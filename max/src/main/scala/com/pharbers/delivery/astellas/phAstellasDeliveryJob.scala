package com.pharbers.delivery.astellas

import java.util.{Date, UUID}

import com.pharbers.common.algorithm.{max_path_obj, phSparkCommonFuncTrait}
import com.pharbers.delivery.astellas.format.{phAstellasHospitalMatchFormat, phAstellasMedicineMatchFormat}
import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.generalactions.xlsxReadingAction
import com.pharbers.panel.format.input.writable.astellas.{phAstellasHospitalMatchWritable, phAstellasMedicineMatchWritable}
import com.pharbers.spark.phSparkDriver
import org.apache.spark.rdd.RDD

/**
  * Created by jeorch on 18-6-5.
  */
class phAstellasDeliveryJob(args: Map[String, String]) extends pActionTrait with phSparkCommonFuncTrait {
    override val name: String = "export_delivery_data_action"
    override val defaultArgs: pActionArgs = NULLArgs

    override def perform(pr: pActionArgs): pActionArgs = {

        val delimiter = 9.toChar.toString

        val mkt = args("mkt")
        val ym_condition = args("ym_condition")
        val maxSearchDF = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get

        val historyFile: String = max_path_obj.p_matchFilePath + args("history_file")
        val productMatchFile: String = max_path_obj.p_matchFilePath + args("product_match_file")

        val sc = phSparkDriver().sc
        val history_rdd = sc.textFile(historyFile)
        val medicine_match = xlsxReadingAction[phAstellasMedicineMatchFormat](productMatchFile, "medicine_match_key")
            .perform(NULLArgs).get.asInstanceOf[RDD[phAstellasMedicineMatchWritable]]

        val maxRDD = maxSearchDF.rdd.map(item =>
            (
                item.getAs[String]("Province"),
                item.getAs[String]("City").toString,
                item.getAs[Int]("Date").toString,
                item.getAs[String]("Product").toString,
                item.getAs[Double]("sum(f_sales)"),
                item.getAs[Double]("sum(f_units)"),
                item.getAs[String]("MARKET").toString
            )
        )

        /**
          * Step 1. Modify medicine_match_rdd in phAstellasMedicineMatchWritable.
          * Step 2. Filter the medicine_match_rdd && distinct
          */

        val medicine_filtered = medicine_match.filter(row =>
            !(!List("可多华", "保列治", "高特灵", "贝可", "得妥", "宁通", "舍尼亭", "托特罗定").contains(row.getRowKey("STANDARD_PRODUCT_NAME")) &&
                List("多沙唑嗪", "特拉唑嗪", "非那雄胺", "托特罗定").contains(row.getRowKey("STANDARD_MOLE_NAME"))))
        val medicine_distinct = medicine_filtered.map(x => x.getRowKey("min2") -> (
            x.getRowKey("STANDARD_PRODUCT_NAME"),
            x.getRowKey("STANDARD_APP2_COD"),
            x.getRowKey("STANDARD_PACK_DES"),
            x.getRowKey("PACK_NUMBER2"),
            x.getRowKey("STANDARD_CORP_NAME"),
            x.getRowKey("STANDARD_MOLE_NAME")
        )).distinct()

        /**
          * Step 3. GroupBy max_result_rdd[Province,City,Date,Product,Market] & sum(f_units),sum(f_sales).
          */

        val max_result_groupBy = maxRDD.map(row =>
            (row._1, row._2, row._3, row._4, row._7) ->
                (row._5, row._6)
        ).reduceByKey((curValue, nextValue) => (curValue._1 + nextValue._1, curValue._2 + nextValue._2)).map(one =>
            one._1._4 -> (one._1._5, one._1._1, one._1._2, one._1._3, one._2._1, one._2._2)
        )

        /**
          * Step 4. Merge max_result with medicine_distinct.
          */

        val max_result_merged = max_result_groupBy.join(medicine_distinct)

        /**
          * Step 5.Split [Product] field have been completed when the medicine.xlsx is loaded
          */

        /**
          * Step 6.Modify some fields have been completed when the medicine.xlsx is loaded
          */

        /**
          * Step 7.Filter max_result_merged by some fields.
          */


        val max_result_filter = max_result_merged.filter(row => !(!(List("粉针剂","注射剂").contains(row._2._2._2)) && List("米开民市场", "佩尔市场").contains(row._2._1._1))
            && !(List("粉针剂", "注射剂", "滴眼剂").contains(row._2._2._2) && row._2._1._1.equals("阿洛刻市场"))
            && !(row._2._2._2.equals("滴眼剂") && row._2._1._1.equals("普乐可复市场"))
            && !(row._2._2._1.equals("保法止"))
        ).filter(row => !List("倍他司汀", "阿魏酰γ-丁二胺/植物生长素", "丙磺舒", "复方别嘌醇").contains(row._2._2._6))
            .map(row => (row._2._1, row._2._2))

        /**
          * Step 8.Select 12 columns and rename them.
          */

        val max_result_renamed = max_result_filter
            .map(row => row._1._2 + delimiter + row._1._3
                + delimiter + row._1._4 + delimiter + row._2._1
                + delimiter + row._2._2 + delimiter + row._2._3
                + delimiter + row._2._4 + delimiter + row._2._5
                + delimiter + row._1._5.toString + delimiter + row._1._6.toString
                + delimiter + row._1._1 + delimiter + row._2._6
            )

        /**
          * Step 9.Union old_delivery_file.
          */

        val title_rdd = history_rdd.context.parallelize(history_rdd.take(1))
        val union_result = title_rdd.union(max_result_renamed).union(history_rdd.filter(x => !x.contains("Province")))

        /**
          * Step 10.save in destPath.
          */

        val maxSearchResultName = UUID.randomUUID().toString
        val exportDataPath = max_path_obj.p_exportPath + maxSearchResultName
        val destFileName = s"${new Date().getTime}-${mkt}-${ym_condition}.csv"
        val destPath = max_path_obj.p_exportPath + destFileName

        union_result.coalesce(1).saveAsTextFile(exportDataPath)

        move2ExportFolder(getResultFileFullPath(exportDataPath), destPath)

        StringArgs(destFileName)
    }

}
