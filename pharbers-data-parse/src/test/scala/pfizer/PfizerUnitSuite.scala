//package pfizer
//
//import com.pharbers.pfizer.impl.phPfizerHandleImpl
//import org.scalatest.FunSuite
//
//import scala.collection.immutable.Map
//
//
///**
//  * Created by clock on 17-9-7.
//  */
//class PfizerUnitSuite extends FunSuite {
//    val args: Map[String, List[String]] = Map(
//        "company" -> List("generatePanel"),
//        "user" -> List("user"),
//        "cpas" -> List("/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/CPA/1705 CPA.xlsx"),
//        "gycxs" -> List("/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/GYCX/1705 GYC.xlsx")
//    )
//    test("load CPA") {
//        new phPfizerHandleImpl(args)
//                .loadCPA("/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/CPA/1705 CPA.xlsx" :: Nil)
//                .foreach(println)
//    }
//
//    test("load GYCX") {
//        new phPfizerHandleImpl(args)
//                .loadGYCX("/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/GYCX/1705 GYC.xlsx" :: Nil)
//                .foreach(println)
//    }
//
//    test("load m1") {
//        new phPfizerHandleImpl(args).load_m1.foreach(println)
//    }
//
//    test("load hos0") {
//        new phPfizerHandleImpl(args).load_hos00.foreach(println)
//    }
//
//    test("load b0") {
//        new phPfizerHandleImpl(args).load_b0("INF").foreach(println)
//    }
//
//    test("test inner join") {
//        val b0 = new phPfizerHandleImpl(args).load_b0("INF")
//        val m1 = new phPfizerHandleImpl(args).load_m1
//        new phPfizerHandleImpl(args).innerJoin(b0, m1, "CPA反馈通用名", "通用名").foreach(println)
//    }
//}
