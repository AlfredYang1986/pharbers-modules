//package pfizer
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.pharbers.pfizer.impl.phPfizerHandleImpl
//import org.scalatest.FunSuite
//import play.api.libs.json.JsString
//
//import scala.collection.immutable.Map
//
///**
//  * Created by clock on 17-9-7.
//  */
//class PfizerSuite extends FunSuite {
//    val gycx_file_local = "/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/GYCX/1705 GYC.xlsx"
//    val cpa_file_local = "/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/CPA/1705 CPA.xlsx"
//    val args: Map[String, List[String]] = Map(
//        "company" -> List("generatePanel"),
//        "user" -> List("user"),
//        "cpas" -> List(cpa_file_local),
//        "gycxs" -> List(gycx_file_local)
//    )
//
//    test("calc ym") {
//        val result = new phPfizerHandleImpl(args).calcYM
//        println(result)
//    }
//
//    test("generate panel file") {
//        val result = new phPfizerHandleImpl(args).generatePanelFile("201705")
//        println(result)
//    }
//
//    test("Pressure test => 50") {
//        val dateformat1 = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"压力测试开始时间" + dateformat1.format(new Date()))
//
//        val gycx_file_local = "/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/GYCX/1705 GYC.xlsx"
//        val cpa_file_local = "/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/CPA/1705 CPA.xlsx"
//
//        for(i <- 1 to 50) {
//            val args: Map[String, List[String]] = Map(
//                "company" -> List("generatePanel"),
//                "user" -> List("user"+i),
//                "cpas" -> List(cpa_file_local),
//                "gycxs" -> List(gycx_file_local)
//            )
//            val parse = new phPfizerHandleImpl(args)
//            val ym = parse.calcYM.asInstanceOf[JsString].value
//            val result = parse.generatePanelFile(ym)
//            println(s"第$i 个完成结果$result")
//        }
//
//        println(s"压力测试结束时间" + dateformat1.format(new Date()))
//    }
//}