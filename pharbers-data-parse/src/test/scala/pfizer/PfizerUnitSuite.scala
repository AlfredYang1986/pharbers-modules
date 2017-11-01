package pfizer

import com.pharbers.panel.pfizer.{panel_file_path, phPfizerHandle}
import org.scalatest.FunSuite

import scala.collection.immutable.Map


/**
  * Created by clock on 17-9-7.
  */
class PfizerUnitSuite extends FunSuite with panel_file_path {
    val args: Map[String, List[String]] = Map(
        "company" -> List("company"),
        "user" -> List("user"),
        "cpas" -> List("1705 CPA.xlsx"),
        "gycxs" -> List("1705 GYC.xlsx")
    )

    test("load CPA") {
        println(phPfizerHandle(args).loadCPA)
    }

    test("test fill hos lst") {
        val fill_hos_lst = phPfizerHandle(args).fill_hos_lst(5)
        println(s"num = ${fill_hos_lst.length}")
        fill_hos_lst.foreach(println)
    }

    test("load GYCX") {
        println(phPfizerHandle(args).loadGYCX)
    }

    test("test calcYM") {
        println(phPfizerHandle(args).calcYM)
    }

    test("load m1") {
        phPfizerHandle(args).load_m1.foreach(println)
    }

    test("load hos0") {
        phPfizerHandle(args).load_hos00("INF").foreach(println)
    }

    test("load b0") {
        phPfizerHandle(args).load_b0("INF").foreach(println)
    }

    test("test inner join") {
        val b0 = phPfizerHandle(args).load_b0("INF")
        val m1 = phPfizerHandle(args).load_m1
        phPfizerHandle(args).innerJoin(b0.toStream, m1.toStream, "CPA反馈通用名", "通用名").foreach(println)
    }
}
