//package com.pharbers.panel.astellas
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.pharbers.paction.actionbase.NULLArgs
//import com.pharbers.panel.{phPanelFilePath, phPanelHeadle}
//import com.pharbers.spark.driver.phSparkDriver
//import org.scalatest.FunSuite
//import play.api.libs.json.JsValue
//
//import scala.collection.immutable.Map
//
///**
//  * Created by clock on 18-1-3.
//  */
//class AstellasSuite extends FunSuite with phPanelFilePath {
//    val sparkDriver = phSparkDriver()
//    val company_name = "8ee0ca24796f9b7f284d931650edbd4b"
//    val file_base = base_path + company_name
//    val cpa_name = "171215恩华2017年10月检索.xlsx"
//    val args: Map[String, List[String]] = Map(
//        "company" -> List(company_name),
//        "uid" -> List("08f1517cd192c5d8f9290c46418e08b1"),
//        "cpas" -> List(cpa_name),
//        "gycxs" -> List("")
//    )
//
//    test("test get markets") {
//        val mktHander = phPanelHeadle(args)
//        println("mkt = " + mktHander.getMarkets)
//    }
//
//    test("test calc ym") {
//        object test extends calcYMActions {
////            val cpa_location = "/mnt/config/FileBase/235f39e3da85c2dee2a2b20d004a8b77/Client/171215恩华2017年10月检索.xlsx"
////            val cpa_location = "/mnt/config/FileBase/235f39e3da85c2dee2a2b20d004a8b77/Client/171115恩华2017年9月检索.xlsx"
//            lazy val cpa_location = "/mnt/config/FileBase/235f39e3da85c2dee2a2b20d004a8b77/Client/170322安斯泰来1月底层检索.xls"
//        }
//        test.perform(NULLArgs)
//    }
//
//    test("test generate panel file") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        def getResult(data: JsValue) = {
//            data.as[Map[String, JsValue]].map { x =>
//                x._1 -> x._2.as[Map[String, JsValue]].map { y =>
//                    y._1 -> y._2.as[List[String]]
//                }
//            }
//        }
//
//        val panelHander = phPanelHeadle(args)
//        val result = getResult(panelHander.getPanelFile(List("201708")))
//        println("result = " + result)
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//    }
//
//    test("Pressure test => 150") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"压力测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        def getResult(data: JsValue) = {
//            data.as[Map[String, JsValue]].map { x =>
//                x._1 -> x._2.as[Map[String, JsValue]].map { y =>
//                    y._1 -> y._2.as[List[String]]
//                }
//            }
//        }
//
//        val panelHander = phPanelHeadle(args)
//
//        for (i <- 1 to 1) {
//            val result = getResult(panelHander.getPanelFile(List("201708")))
//            val panelLst = result.values.flatMap(_.values).toList.flatten
//            print(s"panel $i = " + panelLst.mkString(","))
//        }
//
//        println()
//        println(s"压力测试结束时间" + dateformat.format(new Date()))
//    }
//}
