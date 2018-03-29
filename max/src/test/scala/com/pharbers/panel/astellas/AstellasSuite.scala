//package com.pharbers.panel.nhwa
//
//import java.util.Date
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//import scala.collection.immutable.Map
//import com.pharbers.panel.phPanelHeadle
//import play.api.libs.json.{JsString, JsValue}
//
///**
//  * Created by clock on 18-1-3.
//  */
//class AstellasSuite extends FunSuite {
//    val company_name = "235f39e3da85c2dee2a2b20d004a8b77"
//    val cpa_name = "171215安斯泰来10月底层检索.xlsx"
//    val gycx_name = "2017年1-10月安斯泰来37个通用名7个商品名产品检索.xlsx"
//
//    val args: Map[String, List[String]] = Map(
//        "company" -> List(company_name),
//        "uid" -> List("08f1517cd192c5d8f9290c46418e08b1"),
//        "cpas" -> List(cpa_name),
//        "gycxs" -> List(gycx_name)
//    )
//
//    test("test get markets") {
//        val mktHander = phPanelHeadle(args)
//        println("mkt = " + mktHander.getMarkets)
//    }
//
//    test("test calc ym") {
//        val yms = phPanelHeadle(args).calcYM.asInstanceOf[JsString].value
//        val lst = yms.split(",").toList
//        println(lst)
//    }
//
//    test("test generate panel file") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println()
//        println()
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//        println()
//
//
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
////        val result = getResult(panelHander.getPanelFile(List("201708")))
//        val result = panelHander.getPanelFile(List("201710"))
//
//
//
//        println()
//        println()
//        println("result = " + result)
//        println()
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
