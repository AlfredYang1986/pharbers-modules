//package pfizer
//
//import com.pharbers.pfizer.impl.phPfizerHandleImpl
//import org.scalatest.FunSuite
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
//}
