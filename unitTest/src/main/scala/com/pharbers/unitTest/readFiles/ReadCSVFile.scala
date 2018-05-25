package com.pharbers.unitTest.readFiles

import java.text.DecimalFormat
import org.apache.spark.sql.DataFrame

class ReadCSVFile(areas: List[Map[String,String]], data: DataFrame) {
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
//        val sales = sumArray(0).get(1).formatted("%.1f").toDouble.toString
//        val sales = decimalFormat.format(sumArray(0).get(1)).formatted("%.1f").toString
        val sale = sumArray(0).get(1) match {
            case null => 0
            case _ => sumArray(0).get(1)
        }
        val sales = decimalFormat.format(sale)
        val unit = sumArray(0).get(0) match {
            case null => 0
            case _ => sumArray(0).get(0)
        }
        val units = decimalFormat.format(unit)
        println(Map("hosptalCount" -> hosptalCount, "productCount" -> productCount, "sales" -> sales.toString, "units" -> units.toString))
        Map("hosptalCount" -> hosptalCount, "productCount" -> productCount, "sales" -> sales, "units" -> units.toString)
    }
    
    def formatArea(areamap: Map[String, String]): Map[String, String] ={
        val areaType = areamap("areaType") match {
            case "All" => "1"
            case "ALL" => "1"
            case "CITY" => "City"
//            case "PROVINCES" => "Province"
            case "PROVINCES" => "City"
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
    def apply(areas: List[Map[String,String]], data: DataFrame): ReadCSVFile = new ReadCSVFile(areas, data)
}
