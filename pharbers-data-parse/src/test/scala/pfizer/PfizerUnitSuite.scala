package pfizer

import com.pharbers.memory.pages.pageMemory
import com.pharbers.panel.pfizer.impl.phPfizerHandleImpl
import com.pharbers.panel.pfizer.panel_file_path
import org.scalatest.FunSuite

import scala.collection.immutable.Map


/**
  * Created by clock on 17-9-7.
  */
class PfizerUnitSuite extends FunSuite with panel_file_path {
    val args: Map[String, List[String]] = Map(
        "company" -> List("company"),
        "user" -> List("user"),
        "cpas" -> List("config/company/Client/CPA/1705 CPA.xlsx"),
        "gycxs" -> List("config/company/Client/GYCX/1705 GYC.xlsx")
    )

    test("load CPA") {
        println(new phPfizerHandleImpl(args).loadCPA)
    }

    test("load GYCX") {
        println(new phPfizerHandleImpl(args).loadGYCX)
    }

    test("load calcYM") {
        println(new phPfizerHandleImpl(args).calcYM)
    }

    test("page memory") {
        val page = pageMemory("config/company/Cache/test.cache")
        println(s"t.size = ${page.allLength}")
        println(s"t.pageCount = ${page.pageCount}")

        //        t.allData.zipWithIndex.foreach { x =>
        //            println(s"${x._2} : ${x._1}")
        //        }

        page.pageData(1).zipWithIndex.foreach { x =>
            println(s"${x._2} : ${x._1}")
        }
    }

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

}
