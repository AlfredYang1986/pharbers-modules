package com.pharbers.delivery.astellas

import com.pharbers.paction.actionbase._
import com.pharbers.panel.format.input.writable.astellas.{phAstellasHospitalMatchWritable, phAstellasMedicineMatchWritable}
import org.bson.Document

/**
  * Created by jeorch on 18-3-29.
  */
object phAstellasDeliveryAction {
    def apply(): pActionTrait = new phAstellasDeliveryAction
}

class phAstellasDeliveryAction extends pActionTrait {

    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        val dataMap = pr.asInstanceOf[MapArgs].get
        val hospital_match = dataMap("hospital_match_key").asInstanceOf[RDDArgs[phAstellasHospitalMatchWritable]].get
        val medicine_match = dataMap("medicine_match_key").asInstanceOf[RDDArgs[phAstellasMedicineMatchWritable]].get
        val mongo_rdd = dataMap("mongo_rdd_key").asInstanceOf[RDDArgs[Document]].get

        /**
          * PreStep. If max_result[MongoRDD] has [Province] field, then this step is unnecessary！
          */
        val max_result_with_province = if (mongo_rdd.first().containsKey("Province")){
            println("***************BAOHAN")
            mongo_rdd.map(doc =>
                (
                    doc.get("Province").toString,
                    doc.get("City").toString,
                    doc.get("Date").toString,
                    doc.get("Product").toString,
                    doc.get("f_sales").asInstanceOf[Double],
                    doc.get("f_units").asInstanceOf[Double],
                    doc.get("Market").toString
                )
            )
        } else {
            println("***************NONONO")
            val mongoRDDTuple1 = mongo_rdd.map( row => row.get("Panel_ID").toString -> row)
            val hospitalRDDTuple = hospital_match.map(x => x.getRowKey("PHA_ID") -> x)
            val joinedHospitalRDD = mongoRDDTuple1.leftOuterJoin(hospitalRDDTuple)
            joinedHospitalRDD.map( item =>
                (
                    item._2._2.get.getRowKey("Province"),
                    item._2._1.get("City").toString,
                    item._2._1.get("Date").toString,
                    item._2._1.get("Product").toString,
                    item._2._1.get("f_sales").asInstanceOf[Double],
                    item._2._1.get("f_units").asInstanceOf[Double],
                    item._2._1.get("Market").toString
                )
            )
        }

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
          * Step 3. GroupBy max_result_rdd & sum(f_units),sum(f_sales).
          */

        val max_result_groupBy = max_result_with_province.map(row =>
            (row._1, row._2, row._3, row._4, row._7) ->
                (row._5, row._6)
        ).reduceByKey((curValue, nextValue) => (curValue._1 + nextValue._1, curValue._2 + nextValue._2)).map(one =>
            one._1._4 -> (one._1._5, one._1._1, one._1._2, one._1._3, one._2._1, one._2._2)
        )

        /**
          * Step 4. Merge max_result with medicine_distinct.
          */

        val max_result_merged = max_result_groupBy.leftOuterJoin(medicine_distinct)

        val test = max_result_merged
        test.take(10).foreach(x => println(x))
        println(test.count())

        /**
          * Step 5.Split [Product] field have been completed when the medicine.xlsx is loaded
          */

        /**
          * Step 6.Modify some fields have been completed when the medicine.xlsx is loaded
          */

        /**
          * Step 7.Filter max_result_merged by some fields.
          */

        val max_result_filter = max_result_merged

        /**
          * Step 8.Select 12 columns and rename them.
          */

        /**
          * Step 8.Union old_delivery_file && save in new_delivery_file.
          */

//        StringArgs(test)
        defaultArgs
    }
}