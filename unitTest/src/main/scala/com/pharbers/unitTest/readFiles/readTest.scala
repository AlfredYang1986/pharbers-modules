package com.pharbers.unitTest.readFiles

import java.io.FileInputStream

import com.pharbers.calc.phMaxJob
import com.pharbers.pactions.actionbase.{MapArgs, RDDArgs}
import com.pharbers.panel.astellas.phAstellasPanelJob
import com.pharbers.panel.nhwa.phNhwaPanelJob
import com.pharbers.spark.phSparkDriver
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.spark.sql.SQLContext
import com.pharbers.unitTest.readFiles.ReadExcelFile

object readTest extends App {
//    val xls_file: String = "/home/cui/download/单元测试模板.xlsx"
//    val ins = new FileInputStream(xls_file)
//    val xssfWorkbook = new XSSFWorkbook(ins)
//    val sheet = xssfWorkbook.getSheetAt(1)
//    val lastRowNumber = sheet.getLastRowNum
//    println(lastRowNumber)
//    val a = ReadExcelFile()
//    println(a)
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //readcsvFile  2018/05/02
//    val csvFileName = "a4cf6fbd-da14-41f2-9b34-ab314177df06"
//    lazy val sparkDriver: phSparkDriver = phSparkDriver()
//    val sqlContext = new SQLContext(sparkDriver.sc)
//    val data = sqlContext.read.format("com.databricks.spark.csv")
//            .option("header", "true") //这里如果在csv第一行有属性的话，没有就是"false"
//            .option("inferSchema", true.toString) //这是自动推断属性列的数据类型。
//            .option("delimiter", 31.toChar.toString)
//            .load(s"/mnt/config/Result/${csvFileName}") //文件的路径
//            .withColumnRenamed("f_sales", "sales")
//            .withColumnRenamed("f_units", "units")
//
//    def getcsvResult(areas: List[Map[String,String]]): List[Map[String, Any]] ={
//        val formatedAreas: List[Map[String, String]] = areas.map(areamap => formatArea(areamap))
//        val csvResult: List[Map[String, Any]] = formatedAreas.map(formatedArea => getcsvResult(formatedArea))
//        csvResult
//    }
//
//    def getcsvResult(formatedArea: Map[String, String]): Map[String, Any] ={
//        val city = formatedArea("City")
//        val area = formatedArea("Area")
//        val hosptalsum = data.filter(s"${city} == '${area}'").select("Panel_ID").distinct().count()
//        val productsum = data.filter(s"${city} == '${area}'").select("Product").distinct().count()
//        val sumArray = data.filter(s"${city} == '${area}'").select("units", "sales").agg(Map("Units" -> "sum", "Sales" -> "sum")).collect()
//        val sales = sumArray(0).get(1).formatted("%.1f").toDouble
//        val units = sumArray(0).get(0)
//        Map("hosptalsum" -> hosptalsum, "productsum" -> productsum, "sales" -> sales, "units" -> units)
//    }
//
//    def formatArea(areamap: Map[String, String]): Map[String, String] ={
//        val areaType = areamap("areaType") match {
//            case "All" => "1"
//            case "CITY" => "City"
//            case "PROVINCES" => "Province"
//            case "HOSP" => "Panel_ID"
//            case _ => throw new Exception("Excel文件内容有误")
//        }
//        val area = areamap("area") match {
//            case "全国" => "1"
//            case _ => areamap("area")
//        }
//        Map("City" -> areaType, "Area" -> area)
//    }
    
    
    
    
    
    
    
    
    
    
    
    //redaExcelFile 2018/05/02
//    val xls_file: String = "/home/cui/download/单元测试模板.xlsx"
//    val ins = new FileInputStream(xls_file)
//    val xssfWorkbook = new XSSFWorkbook(ins)
//    val sheet = xssfWorkbook.getSheetAt(1)
//    val firstRowNumber = sheet.getFirstRowNum
//    val lastRowNumber = sheet.getLastRowNum
//    val excelList: List[Map[String, String]] = Range(2, lastRowNumber).map(row => getRows(row)).toList
//    def getRows(row: Int): Map[String, String] ={
//        Map("areaType" -> sheet.getRow(row).getCell(3).toString,
//            "area" -> sheet.getRow(row).getCell(2).toString,
//            "hosptalnum" -> sheet.getRow(row).getCell(5).toString,
//            "productnum" -> sheet.getRow(row).getCell(6).toString,
//            "sales" -> sheet.getRow(row).getCell(7).toString,
//            "units" -> sheet.getRow(row).getCell(8).toString
//        )
//    }
//    println(excelList)
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //  val panelResult = phAstellasPanelJob("/mnt/config/Client/astl_cpa-12.xlsx", "/mnt/config/Client/astl_gycx_1-12.xlsx", "201712", "Allelock").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//    val panelResult = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//    println(panelResult)
    //  val maxResultMap = phMaxJob(panelResult, "astellas/UNIVERSE_Allelock_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
//    val maxResult = phMaxJob(panelResult, "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
//    println(maxResult)
    
    
//    val csvFileName = "a4cf6fbd-da14-41f2-9b34-ab314177df06"
//    lazy val sparkDriver: phSparkDriver = phSparkDriver()
//    val sqlContext = new SQLContext(sparkDriver.sc)
//    val data =sqlContext.read.format("com.databricks.spark.csv")
//            .option("header","true") //这里如果在csv第一行有属性的话，没有就是"false"
//            .option("inferSchema",true.toString)//这是自动推断属性列的数据类型。
//            .option("delimiter",31.toChar.toString)
//            .load(s"/mnt/config/Result/${csvFileName}")//文件的路径
//    data.select("Province","Porduct").groupBy("Province","Porduct")

//    val sumArray = data.select("Province","f_sales","f_units").groupBy("Province").agg(Map("f_sales" -> "sum", "f_units" -> "sum")).collect()
//    val sales = sumArray(0).get(1)
//    val units = sumArray(1).get(2)
//    val productsum = data.filter("Province='广东'").select("Product").distinct().count()
////    val productsummr = data.filter("Privince='广东'")
////    data.withColumnRenamed("f_sales","sales").show()
//    println("sales" + sales)
//    println("units" + units)
//    println(productsum)

}
