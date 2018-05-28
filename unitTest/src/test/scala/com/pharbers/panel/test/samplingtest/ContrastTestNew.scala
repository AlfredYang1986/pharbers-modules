//package com.pharbers.panel.test.samplingtest
//
//import java.text.DecimalFormat
//
//import com.pharbers.unitTest.getResult.{ExecuteJob, compareResult}
//import com.pharbers.unitTest.readFiles.{LoadCSVFile, ReadExcelFileTemp, readXmlFile}
//import org.apache.spark.sql.DataFrame
//import org.apache.spark.sql.functions.expr
//import org.specs2.specification.core.Fragment
//
//class ContrastTestNew extends org.specs2.mutable.Specification {
//    val companylst = readXmlFile().readXmlFile()
//    val result = companylst.map(ExecuteJob(_).getresultPath)
//    result.indices.foreach(i =>
//        s"开始对比 ${result(i)("resultMatch_file")}" >> {
//            val excelResult = ReadExcelFileTemp(result(i)("resultMatch_file")).getAllResult()
//            val data = LoadCSVFile().getDataSet(result(i)("maxResult"))
//            val provinceData = data.select("Province", "Panel_ID", "Product", "sales", "units")
//                    .groupBy("Province")
//                    .agg(expr("count(distinct Panel_ID)") as "City", expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//            val cityData = data.select("City", "Panel_ID", "Product", "sales", "units")
//                    .groupBy("City")
//                    .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//            val hosptalData = data.select("Panel_ID", "Product", "sales", "units")
//                    .groupBy("Panel_ID")
//                    .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//            val allData = data.select("Date", "Panel_ID", "Product", "sales", "units")
//                    .groupBy("Date")
//                    .agg(expr("count(distinct Panel_ID)"), expr("count(distinct Product)"), expr("sum(sales)"), expr("sum(units)"))
//            Fragment.foreach(excelResult.indices) { j =>
//                "Row " + (j + 2) ! {
//                    compareResult().isSame(excelResult(j), getAreaResult(formatArea(excelResult(j)), formatArea(excelResult(j))("City") match {
//                        case "all" => allData
//                        case "Province" => provinceData
//                        case "City" => cityData
//                        case "Panel_ID" => hosptalData
//                    })) must_== true
//                }
//            }
//        }
//    )
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
//            case "全国" => "1"
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
//}
