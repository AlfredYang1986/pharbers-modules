//import java.util.Date
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//import scala.collection.immutable.Map
//import com.pharbers.panel.phGeneratePanel
//import play.api.libs.json.{JsString, JsValue}
//
///**
//  * Created by clock on 18-1-3.
//  */
//class NhwaSuite extends FunSuite {
//    val cpa_file_local = "171016恩华2017年8月检索.xlsx"
//    val cpa_file_local2 = "to医药魔方 恩华CPA原始数据2015.1-2017.6.xlsx"
//    val args: Map[String, List[String]] = Map(
//        "company" -> List("8ee0ca24796f9b7f284d931650edbd4b"),
//        "uid" -> List("08f1517cd192c5d8f9290c46418e08b1"),
//        "cpas" -> List(cpa_file_local2),
//        "gycxs" -> List("")
//    )
//
//    test("get markets") {
//        val mktHander = phGeneratePanel(args)
//        println("mkt = " + mktHander.getMarkets)
//    }
//
//    test("calc ym") {
//        val ymHander = phGeneratePanel(args)
//        val yms = ymHander.calcYM.asInstanceOf[JsString].value
//        val lst = yms.split(",").toList
//        println(lst)
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
//        val panelHander = phGeneratePanel(args)
//        val result = getResult(panelHander.getPanelFile(List("201701")))
//        println("result = " + result)
////        val panelLst = result.values.flatMap(_.values).toList.flatten
////        panelLst.foreach(x => println(s"panel = $x"))
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
//        val panelHander = phGeneratePanel(args)
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
