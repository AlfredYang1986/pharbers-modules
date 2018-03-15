package com.pharbers.panel.nhwa

import java.io.File
import java.util.UUID
import play.api.libs.json.JsValue
import org.apache.spark.sql.DataFrame
import play.api.libs.json.Json.toJson
import com.pharbers.panel.phPanelTrait
import com.pharbers.spark.driver.phSparkDriver
import org.apache.spark.sql.types.{DoubleType, LongType}

/**
  * Created by clock on 18-3-6.
  */
trait phNhwaPanel extends phPanelTrait  {
    val sparkDriver: phSparkDriver
    val m1_location: String
    val hos_location: String
    val b0_location: String
    val not_arrival_hosp_location: String
    val not_published_hosp_location: String
    val full_hosp_location: String

    val cpa_location: String
    val output_location: String

    override def getPanelFile(ym: List[String], mkt: String, t: Int = 0, c: Int = 0): JsValue = {

        val result = ym.map{x =>
            val c0 = fullCPA(cpa_location, x)
            val m1 = load(m1_location).distinct()
            val hos0 = load(hos_location).filter(s"DOI = '$mkt'")
            val b0 = load(b0_location)
            val m1_c = m1.join(b0, b0("通用名_原始") === m1("通用名"))
            val c2 = c0.join(hos0, c0("HOSPITAL_CODE") === hos0("ID"))
            val panel = trimPanel(c2, m1_c)

            x -> toJson(List(writePanel(panel)))
        }.toMap
        sparkDriver.ss.stop()
        toJson(result)
    }

    private def load(file_path: String) = sparkDriver.csv2RDD(file_path, delimiter = 31.toChar.toString)

    private def fullCPA(cpa_path: String, ym: String): DataFrame = {

        val month = ym.takeRight(2).toInt.toString
        val c0 = load(cpa_path).filter(s"YM = '$ym'")

        val nah = load(not_arrival_hosp_location)
                .withColumnRenamed("月份", "month")
                .filter(s"month like '%$month%'")
                .withColumnRenamed("医院编码", "ID")
                .select("ID")
        val nph = load(not_published_hosp_location)
                .withColumnRenamed("id", "ID")
        val nh = nah.union(nph).distinct()

        val c1 = c0.join(nh, c0("HOSPITAL_CODE") === nh("ID"), "left").filter("ID is null").drop("ID")

        val fhd0 = load(full_hosp_location).filter(s"MONTH = $month").drop("x")
        val fhd1 = nh.join(fhd0, fhd0("HOSPITAL_CODE") === nh("ID")).drop("ID").select(c1.columns.head, c1.columns.tail:_*)

        c1.union(fhd1)
    }

    private def trimPanel(c2: DataFrame, m1_c: DataFrame): DataFrame = {
        import sparkDriver.ss.implicits._
        val temp = c2.join(m1_c, c2("min1") === m1_c("min1"))
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

        val temp_panel = output_location + UUID.randomUUID.toString
        val panel_location = temp_panel + ".csv"

        panel.coalesce(1).write
                .format("csv")
                .option("delimiter", 31.toChar.toString)
                .save(temp_panel)

        val tempFile = getAllFile(temp_panel).find(_.endsWith(".csv")) match {
            case None => throw new Exception("未找到文件")
            case Some(file) => file
        }
        new File(tempFile).renameTo(new File(panel_location))
        delFile(temp_panel)

        panel_location
    }
}
