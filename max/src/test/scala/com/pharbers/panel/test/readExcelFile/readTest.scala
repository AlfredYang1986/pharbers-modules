package com.pharbers.panel.test.readExcelFile

import java.io.FileInputStream

import com.pharbers.calc.phMaxJob
import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.astellas.phAstellasPanelJob
import com.pharbers.panel.nhwa.phNhwaPanelJob
import com.pharbers.spark.phSparkDriver
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.spark.sql.SQLContext

object readTest extends App {
//  val panelResult = phAstellasPanelJob("/mnt/config/Client/astl_cpa-12.xlsx", "/mnt/config/Client/astl_gycx_1-12.xlsx", "201712", "Allelock").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
  val panelResult = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
  println(panelResult)
//  val maxResultMap = phMaxJob(panelResult, "astellas/UNIVERSE_Allelock_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
  val maxResult = phMaxJob(panelResult, "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
  lazy val sparkDriver: phSparkDriver = phSparkDriver()
  val sqlContext = new SQLContext(sparkDriver.sc)
  val data =sqlContext.read.format("com.databricks.spark.csv")
    .option("header","true") //这里如果在csv第一行有属性的话，没有就是"false"
    .option("inferSchema",true.toString)//这是自动推断属性列的数据类型。
    .option("delimiter",31.toChar.toString)
    .load(s"/mnt/config/Result/${maxResult}")//文件的路径
  data.select("f_sales").show()
  data.select("f_sales").show()
}
