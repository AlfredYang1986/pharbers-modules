//package pfizer
//
//import java.util.Date
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//import scala.collection.immutable.Map
//import com.pharbers.panel.phGeneratePanel
//import play.api.libs.json.{JsString, JsValue}
//
///**
//  * Created by clock on 17-9-7.
//  */
//class PfizerSuite extends FunSuite {
//    val cpa_file_local = "CPA混合版.xlsx"
//    val gycx_file_local = "GYC混合版.xlsx"
//    val args: Map[String, List[String]] = Map(
//        "company" -> List("fea9f203d4f593a96f0d6faa91ba24ba"),
//        "uid" -> List("30ed0dc130abf22c5cfcb0efbd0e0cb7"),
//        "cpas" -> List(cpa_file_local),
//        "gycxs" -> List(gycx_file_local)
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
//        val result = getResult(panelHander.getPanelFile(List("201611","201704")))
//        println("result = " + result)
//        val panelLst = result.values.flatMap(_.values).toList.flatten
////        panelLst.foreach(x => println(s"panel = $x"))
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//    }
//
//    test("Pressure test => 50") {
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
//        for (i <- 1 to 50) {
//            val result = getResult(panelHander.getPanelFile(List("201704")))
//            val panelLst = result.values.flatMap(_.values).toList.flatten
//            print(s"panel $i = " + panelLst.mkString(","))
//        }
//
//        println()
//        println(s"压力测试结束时间" + dateformat.format(new Date()))
//    }
//}