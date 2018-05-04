package com.pharbers.unitTest.readFiles

import java.text.DecimalFormat

import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.SQLContext

class ReadCSVFile(csvFileName: String, areas: List[Map[String,String]]) {
    lazy val sparkDriver: phSparkDriver = phSparkDriver()
    val sqlContext = new SQLContext(sparkDriver.sc)
    val data = sqlContext.read.format("com.databricks.spark.csv")
            .option("header", "true") //这里如果在csv第一行有属性的话，没有就是"false"
            .option("inferSchema", true.toString) //这是自动推断属性列的数据类型。
            .option("delimiter", 31.toChar.toString)
            .load(s"/mnt/config/Result/$csvFileName") //文件的路径
            .withColumnRenamed("f_sales", "sales")
            .withColumnRenamed("f_units", "units")
    
    def getCsvResult(): List[Map[String, String]] ={
        val formatedAreas: List[Map[String, String]] = areas.map(formatArea)
        val csvResult: List[Map[String, String]] = formatedAreas.map(getAreaResult)
        csvResult
    }
    
    def getAreaResult(formatedArea: Map[String, String]): Map[String, String] ={
        val title = formatedArea("City")
        val area = formatedArea("Area")
        val decimalFormat = new DecimalFormat("###################.##################")
        val hosptalCount = data.filter(s"$title == '$area'").select("Panel_ID").distinct().count().toString
        val productCount = data.filter(s"$title == '$area'").select("Product").distinct().count().toString
        val sumArray = data.filter(s"$title == '$area'").select("units", "sales").agg(Map("Units" -> "sum", "Sales" -> "sum")).collect()
        val sales = sumArray(0).get(1).formatted("%.1f").toDouble.toString
        val units = decimalFormat.format(sumArray(0).get(0))
        Map("hosptalCount" -> hosptalCount, "productCount" -> productCount, "sales" -> sales, "units" -> units.toString)
    }
    
    def formatArea(areamap: Map[String, String]): Map[String, String] ={
        val areaType = areamap("areaType") match {
            case "All" => "1"
            case "CITY" => "City"
            case "PROVINCES" => "Province"
            case "HOSP" => "Panel_ID"
            case _ => throw new Exception("Excel文件内容有误")
        }
        val area = areamap("area") match {
            case "全国" => "1"
            case _ => areamap("area")
        }
        Map("City" -> areaType, "Area" -> area)
    }
}

object ReadCSVFile{
    def apply(csvFileName: String, areas: List[Map[String,String]]): ReadCSVFile = new ReadCSVFile(csvFileName,areas)
}
