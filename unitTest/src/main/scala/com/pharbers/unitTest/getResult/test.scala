package com.pharbers.unitTest.getResult

import java.io.FileInputStream
import java.text.DecimalFormat
import java.util

import com.pharbers.calc.phMaxJob
import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.astellas.phAstellasPanelJob
import com.pharbers.panel.pfizer.phPfizerPanelJob
import com.pharbers.spark.phSparkDriver
import com.pharbers.unitTest.builder.{CompanyParamter, CompanyParamterFactory}
import com.pharbers.unitTest.readFiles._
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.sql.functions.expr

import scala.collection.mutable
import scala.xml.XML

object test extends App {
    //    val a = ReadExcelFile().getResult(3)
    //    println(a)
    
    //    val result = GetResult().getResult
    //    val csvResult = result.head
    //    val excelResult = result.last
    //    println(csvResult)
    //    println(excelResult)
    
    
    //
    //    val result = GetResult().getResult
    //    for(i: Int <- result.indices){
    //        val csvResult = ReadCSVFile(result(i)("maxResult"), ReadExcelFile(result(i)("resultMatch_file")).getArea()).getCsvResult()
    //        val excelResult = ReadExcelFile(result(i)("resultMatch_file")).getExcelResult
    //        for (j: Int <- csvResult.indices){
    //            println(compareResult().isSame(excelResult(j),csvResult(j)))
    //        }
    //    }
    
    //    val result = GetResult().getResult
    //    val csvResult = ReadCSVFile(result(0)("maxResult"), ReadExcelFile(result(0)("resultMatch_file")).getArea()).getCsvResult()
    //    val excelResult = ReadExcelFile(result(0)("resultMatch_file")).getExcelResult
    //    println(csvResult)
    //    println(excelResult)
    
    //20180510辉瑞结果生成
    //    val panelResult = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", "INF").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
    //    println(panelResult)
    //    val result = phMaxJob(panelResult, "pfizer/universe_INF_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
    //    println(result)
    
    
    //    val lst = List(Map("areaType" -> "All","area" -> "全国"), Map("areaType" -> "PROVINCES", "area" -> "广东"))
    //    val result = ReadCSVFile("e8e577b8-445e-4384-9079-22062c2fa53c", lst).getCsvResult()
    //    println(result)
    
    //20180508测试用
    //    val result = GetResult().getResult
    //    println("maxresult$excelresult="+result)
    //    result.indices.foreach(i =>a(i))
    //    def a(i: Int): Unit ={
    //        val csvResult = ReadCSVFile(ReadExcelFileTemp(result(i)("resultMatch_file")).getArea(), LoadCSVFile().getDataSet(result(i)("maxResult"))).getCsvResult()
    //        val excelResult = ReadExcelFileTemp(result(i)("resultMatch_file")).getExcelResult
    //        csvResult.indices.foreach(j => println(compareResult().isSame(excelResult(j), csvResult(j))))
    //    }
    
    //    val xls_file = """/home/cui/download/Pfizer/线下/MaxResult_201802/ CNS_Z _MAX_Result.xlsx"""
    //    val ins = new FileInputStream(xls_file)
    //    val xssfWorkbook = new XSSFWorkbook(ins)
    //    val sheet = xssfWorkbook.getSheetAt(0)
    //    val lastRowNumber = sheet.getLastRowNum
    //    println(lastRowNumber)
    
    //20180509测试DataFrame
    //    val data = LoadCSVFile().getDataSet("3b9b8b54-bc48-49f5-bbb1-a04fbce7053b")
    //    val city = data.select("City", "Panel_ID", "Product", "sales", "units").groupBy("City")
    //    data.select("City","Panel_ID", "Product", "sales", "units").groupBy("City").count().show()
    //    println(a.getClass)
    //    println(a)
    
    
    //20180510测试读取csv文件并处理用
//        val sparkdriver = phSparkDriver()
//        import sparkdriver.ss.implicits._
//        import org.apache.spark.sql.functions._
//        val data = LoadCSVFile().getDataSet("7ade1340-9089-465b-b274-5b7e3c1066c2")
//        val allData = data.select("City", "Panel_ID", "Product", "sales", "units")
//                .groupBy("City")
//                .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//        val a = allData.filter("City == '阿克苏'").collect()
//        println(a isEmpty)
        
//        val cityData = data.select("City", "Panel_ID", "Product", "sales", "units")
//            .groupBy("City")
//            .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//        val a = cityData.filter("City =='阿坝藏族羌族自治州'")
//        val a = data.filter("City == '阿坝藏族羌族自治州'")
//                .select("City","Panel_ID", "Product", "sales", "units")
//                .groupBy("City")
//                .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)")).collect().toList
//        println(a.head)
    
//    val companylst = readXmlFile().readXmlFile()
//    val result = companylst.map(ExecuteJob(_).getresultPath)
//    for (i <- result.indices) {
//        val excelResult = ReadExcelFileTemp(result(i)("resultMatch_file")).getAllResult()
//        val data = LoadCSVFile().getDataSet(result(i)("maxResult"))
//        val provinceData = data.select("Province", "Panel_ID", "Product", "sales", "units")
//                .groupBy("Province")
//                .agg(expr("count(distinct Panel_ID)") as "City", expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//        val cityData = data.select("City", "Panel_ID", "Product", "sales", "units")
//                .groupBy("City")
//                .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//        val hosptalData = data.select("Panel_ID", "Product", "sales", "units")
//                .groupBy("Panel_ID")
//                .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//        val allData = data.select("Date", "Panel_ID", "Product", "sales", "units")
//                .groupBy("Date")
//                .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//        for (j <- excelResult.indices) {
//            println(compareResult().isSame(excelResult(j), getAreaResult(formatArea(excelResult(j)), formatArea(excelResult(j))("City") match {
//                case "all" => allData
//                case "Province" => provinceData
//                case "City" => cityData
//                case "Panel_ID" => hosptalData
//            })))
//
//        }
//    }
//
//
//    def formatArea(areamap: Map[String, String]): Map[String, String] = {
//        val areaType = areamap("areaType") match {
//            case "All" => "all"
//            case "ALL" => "all"
//            case "CITY" => "City"
//            //            case "PROVINCES" => "Province"
//            case "PROVINCES" => "City"
//            case "HOSP" => "Panel_ID"
//            case _ => throw new Exception("Excel文件内容有误")
//        }
//        val area = areamap("area") match {
//            case "全国" => "all"
//            case _ => areamap("area")
//        }
//        Map("City" -> areaType, "Area" -> area)
//    }
//
//    def getAreaResult(formatedArea: Map[String, String], data: DataFrame): Map[String, String] = {
//        val title = formatedArea("City")
//        val area = formatedArea("Area")
//        val decimalFormat = new DecimalFormat("###################.##################")
//        val resultList = title match {
//            case "all" => data.collect()
//            case _ => data.filter(s"$title == '$area'").collect()
//        }
//        resultList isEmpty match {
//            case true => Map("hosptalCount" -> "0",
//                "productCount" -> "0",
//                "sales" -> "0",
//                "units" -> "0"
//            )
//            case false => Map("hosptalCount" -> resultList(0)(1).toString,
//                "productCount" -> resultList(0)(2).toString,
//                "sales" -> resultList(0)(3).toString,
//                "units" -> resultList(0)(4).toString)
//        }
//    }


//    val sparkdriver = phSparkDriver()
//    import sparkdriver.ss.implicits._
//    lazy val sparkDriver: phSparkDriver = phSparkDriver()
//    val sqlContext = new SQLContext(sparkDriver.sc)
    
//    val a = util.Arrays.asList("qwe")
//    println(a.getClass)
//    val b = String.join(",", a)
//    println(b)
    
//    val keys = util.Arrays.asList[String]()
//    val flags = List(true, false, true)
//    val strs = List("str1", "str2", "str3")
//    strs.foreach(key => false match {
//        case true => ""
//        case false => keys.add(key)
//    })
    
    
//    val lst = List(("", "String2", "String3", "sales: 1000"), ("str1", "str2", "str3", "units: 1000"))
//    val lstDF = lst.toDF("str1", "str2", "str3", "error","lst")
//    lstDF.coalesce(1).write
//            .format("csv")
//            .option("header", value = true)
//            .option("delimiter", 31.toChar.toString)
//            .save("/mnt/config/test_001")
    
//    val companylst = readXmlFile().readXmlFile()
//    val company: String = companylst(0).getCompany
//    println(company)
//    val lst = List("lst")
    
    val a = ReadExcelFileTemp("/home/cui/download/Pfizer/线下/MaxResult_201802/INF _MAX_Result.xlsx").getExcelFileResult()
    a.map(b => println(b))
}
