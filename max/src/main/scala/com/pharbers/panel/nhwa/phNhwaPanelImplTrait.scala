package com.pharbers.panel.nhwa

import java.io.File
import java.util.UUID
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import org.apache.spark.sql.DataFrame
import com.pharbers.spark.driver.phSparkDriver
import org.apache.spark.sql.types.{DoubleType, LongType}

/**
  * Created by clock on 18-3-6.
  */
trait phNhwaPanelImplTrait {
    val sparkDriver: phSparkDriver

    val cpa_file: String
    val gycx_file: String

    val product_match_file: String
    val universe_file: String
    val markets_match_file: String
    val not_arrival_hosp_file: String
    val not_published_hosp_file: String
    val full_hosp_file: String

    val output_location: String

    def getPanelFile(ym: List[String], mkt: String, t: Int = 0, c: Int = 0): JsValue = {

        val result = ym.map{x =>
            val full_cpa = fullCPA(cpa_file, x)
            val product_match = loadProductMatch(product_match_file)
            val markets_match = load(markets_match_file)
//            val universe = load(universe_file.replace("##market##", mkt)).filter(s"DOI like '$mkt'")
            val universe = loadUniverse(universe_file, mkt)
            val markets_product_match = product_match.join(markets_match, markets_match("通用名_原始") === product_match("通用名"))
            val filted_panel = full_cpa.join(universe, full_cpa("HOSPITAL_CODE") === universe("ID"))
            val panel = trimPanel(filted_panel, markets_product_match)
            x -> toJson(List(writePanel(panel)))
        }.toMap
        sparkDriver.ss.stop()
        toJson(result)
    }

    private def load(file_path: String) = sparkDriver.csv2RDD(file_path, delimiter = 31.toChar.toString)

    private def fullCPA(cpa: String, ym: String): DataFrame = {

        val filter_month = ym.takeRight(2).toInt.toString
        val primal_cpa = load(cpa).filter(s"YM like '$ym'")
        val not_arrival_hosp = load(not_arrival_hosp_file)
                .withColumnRenamed("月份", "month")
                .filter(s"month like '%$filter_month%'")
                .withColumnRenamed("医院编码", "ID")
                .select("ID")
        val not_published_hosp = load(not_published_hosp_file)
                .withColumnRenamed("id", "ID")
        val miss_hosp = not_arrival_hosp.union(not_published_hosp).distinct()
        val reduced_cpa = primal_cpa.join(miss_hosp, primal_cpa("HOSPITAL_CODE") === miss_hosp("ID"), "left").filter("ID is null").drop("ID")
        val full_hosp_id = load(full_hosp_file).filter(s"MONTH like $filter_month")
        val full_hosp = miss_hosp.join(full_hosp_id, full_hosp_id("HOSPITAL_CODE") === miss_hosp("ID")).drop("ID").select(reduced_cpa.columns.head, reduced_cpa.columns.tail:_*)

        import sparkDriver.ss.implicits._
        reduced_cpa.union(full_hosp).withColumn("HOSPITAL_CODE", 'HOSPITAL_CODE.cast(LongType))
    }

    private def loadProductMatch(product_match_file: String): DataFrame = {

        load(product_match_file)
                .withColumnRenamed("药品名称", "NAME")
                .withColumnRenamed("商品名", "PRODUCT_NAME")
                .withColumnRenamed("剂型", "APP2_COD")
                .withColumnRenamed("规格", "PACK_DES")
                .withColumnRenamed("包装数量", "PACK_NUMBER")
                .withColumnRenamed("生产企业", "CORP_NAME")
                .withColumnRenamed("min1_标准", "min2")
                .selectExpr("concat(PRODUCT_NAME,APP2_COD,PACK_DES,PACK_NUMBER,CORP_NAME) as min1", "min2", "NAME")
                .withColumnRenamed("min2", "min1_标准")
                .withColumnRenamed("NAME", "通用名")
                .distinct()
    }

    private def loadUniverse(universe_file: String, mkt: String): DataFrame = {

        import sparkDriver.ss.implicits._
        load(universe_file)
                .withColumnRenamed("样本医院编码", "ID")
                .withColumnRenamed("PHA医院名称", "HOSP_NAME")
                .withColumnRenamed("PHA ID", "HOSP_ID")
                .withColumnRenamed("市场", "DOI")
                .withColumnRenamed("If Panel_All", "SAMPLE")
                .filter("SAMPLE like '1'")
                .selectExpr("ID", "HOSP_NAME", "HOSP_ID", "DOI", "DOI as DOIE")
                .filter(s"DOI like '$mkt'")
                .withColumn("ID", 'ID.cast(LongType))
    }

    private def trimPanel(filted_panel: DataFrame, markets_product_match: DataFrame): DataFrame = {
        import sparkDriver.ss.implicits._
        val temp = filted_panel.join(markets_product_match, filted_panel("min1") === markets_product_match("min1"))
                .withColumn("ID", 'ID.cast(LongType))
                .withColumnRenamed("HOSP_NAME", "Hosp_name")
                .withColumnRenamed("YM", "Date")
                .withColumnRenamed("min1_标准", "Prod_Name")
                .withColumn("VALUE", 'VALUE.cast(DoubleType))
                .withColumnRenamed("VALUE", "Sales")
                .withColumn("STANDARD_UNIT", 'STANDARD_UNIT.cast(DoubleType))
                .withColumnRenamed("STANDARD_UNIT", "Units")
                .selectExpr("ID", "Hosp_name", "Date",
                    "Prod_Name", "Prod_Name as Prod_CNAME", "HOSP_ID", "Prod_Name as Strength",
                    "DOI", "DOIE", "Units", "Sales")

        temp.groupBy("ID", "Hosp_name", "Date", "Prod_Name", "Prod_CNAME", "HOSP_ID", "Strength", "DOI", "DOIE")
                .agg(Map("Units" -> "sum", "Sales" -> "sum"))
                .withColumnRenamed("sum(Units)", "Units")
                .withColumnRenamed("sum(Sales)", "Sales")
    }

    private def writePanel(panel: DataFrame): String = {
        def getAllFile(dir: String): Array[String] = {
            val list = new File(dir).listFiles()
            list.flatMap {file =>
                if (file.isDirectory) {
                    getAllFile(file.getAbsolutePath)
                } else {
                    Array(file.getAbsolutePath)
                }
            }
        }

        def delFile(dir: String): Unit = {
            val parent = new File(dir)
            val list = parent.listFiles()
            list.foreach {file =>
                if (file.isDirectory) {
                    delFile(file.getAbsolutePath)
                } else {
                    file.delete()
                }
            }
            parent.delete()
        }

        val temp_name = UUID.randomUUID.toString
        val panel_name = temp_name + ".csv"
        val temp_panel_dir = output_location + temp_name
        val panel_location = output_location + panel_name

        panel.coalesce(1).write
                .format("csv")
                .option("delimiter", 31.toChar.toString)
                .save(temp_panel_dir)

        val tempFile = getAllFile(temp_panel_dir).find(_.endsWith(".csv")) match {
            case None => throw new Exception("not single file")
            case Some(file) => file
        }
        new File(tempFile).renameTo(new File(panel_location))
        delFile(temp_panel_dir)

        panel_name
    }
}
