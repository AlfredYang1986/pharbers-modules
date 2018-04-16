package com.pharbers.max

import com.pharbers.calc.phMaxJob
import com.pharbers.pactions.actionbase.{DFArgs, MapArgs}
import com.pharbers.panel.nhwa.phNhwaPanelJob
import org.specs2.execute.Result
import org.specs2.mutable.Specification
import org.specs2.specification.core.{Fragment, Fragments}

/**
  * Created by jeorch on 18-4-13.
  */
class MaxPressureTest extends Specification {

    override def is =
        s2"""
    This is a specification to check the max result correctness
    The max result should
        successfully pass max calc                                               ${doLoopJob(3)}
                                                                 """

//    (1 to 3).foldLeft(Fragments.empty)((res, i) => res.append("test "+i ! {
//        e2
//    }))

    def e2 = {

        val panelResult = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
        println(s"panelResult = ${panelResult}")
        panelResult mustNotEqual ""

        val maxResultMap = phMaxJob(s"$panelResult").perform().asInstanceOf[MapArgs]

        val maxDF = maxResultMap.get("max_calc_action").asInstanceOf[DFArgs].get
        println(s"maxResult.count = ${maxDF.count()}")
        val finalResult = maxDF.agg(Map("f_sales" -> "sum", "f_units" -> "sum"))
        val f_sales = finalResult.collect()(0).get(0)
        val f_units = finalResult.collect()(0).get(1)
        println(s"f_sales = ${f_sales}")
        println(s"f_units = ${f_units}")
        f_sales mustNotEqual 0.0
        f_units mustNotEqual 0.0

        val compareResultDF = maxResultMap.get("compare_result").asInstanceOf[DFArgs].get
        println(s"compareResult.count = ${compareResultDF.count()}")
        val c_finalResult = compareResultDF.agg(Map("f_sales" -> "sum", "f_units" -> "sum"))
        val c_f_sales = c_finalResult.collect()(0).get(0)
        val c_f_units = c_finalResult.collect()(0).get(1)
        println(s"c_f_sales = ${c_f_sales}")
        println(s"c_f_units = ${c_f_units}")
        ~=(f_sales, c_f_sales, 1.0E-3) mustEqual true
        ~=(f_units, c_f_units, 1.0E-3) mustEqual true
    }

    def ~=(x: Any, y: Any, precision: Double) = {
        if ((x.asInstanceOf[Double] - y.asInstanceOf[Double]).abs < precision) true else false
    }

    def doLoopJob(count: Int): Boolean = {
        var canDoJob: Boolean = false
        canDoJob = if(count == 1) true else doLoopJob(count - 1)
        e2
        canDoJob mustEqual true
        canDoJob
    }

    def doLoopJob2(count: Int): Boolean = {
        val panelResult1 = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201711", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
        println(s"panelResult = ${panelResult1}")

        val panelResult2 = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
        println(s"panelResult = ${panelResult2}")

        true
    }

}

