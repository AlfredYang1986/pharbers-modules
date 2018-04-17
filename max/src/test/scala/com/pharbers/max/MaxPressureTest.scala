//package com.pharbers.max
//
//import com.pharbers.calc.phMaxJob
//import com.pharbers.pactions.actionbase.{DFArgs, MapArgs}
//import com.pharbers.panel.nhwa.phNhwaPanelJob
//import com.pharbers.panel.panel_path_obj
//import com.pharbers.spark.phSparkDriver
//import org.specs2.mutable.{Before, Specification}
//import org.specs2.specification.BeforeAll
//import org.specs2.specification.core.{Fragment, Fragments}
//
///**
//  * Created by jeorch on 18-4-13.
//  */
//
//class MaxPressureTest extends Specification with BeforeAll {
//
//    var compareResultCount: Long = 0
//    var compareResultSales: Double = 0.0
//    var compareResultUnits: Double = 0.0
//
//    override def beforeAll: Unit = {
//        println("Do beforeAll")
//        val compareResultDF = phSparkDriver().csv2RDD(panel_path_obj.p_resultPath + "00_luo_00/Nhwa_Max_1712.csv", ",")
//        compareResultCount = compareResultDF.count()
//        val compareResultSum = compareResultDF.agg(Map("f_sales" -> "sum", "f_units" -> "sum")).collect()
//        compareResultSales = compareResultSum(0).get(0).asInstanceOf[Double]
//        compareResultUnits = compareResultSum(0).get(1).asInstanceOf[Double]
//        println(s"compareResultCount = ${compareResultCount}")
//        println(s"compareResultSales = ${compareResultSales}")flatten
//        println(s"compareResultUnits = ${compareResultUnits}")
//    }
//
//    override def is =
//        s2"""
//    This is a specification to check the max result correctness
//    The max result should
//        successfully pass max calc                                               ${doLoopJobs(10)}
//                                                                 """
//
////    (1 to 3).foldLeft(Fragments.empty)((res, i) => res.append("test "+i ! {
////        doMaxJob(i)
////    }))
//
//    def doMaxJob(index: Int) = {
//        val panelResult = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//        println(s"${index}-panelResult = ${panelResult}")
//        panelResult mustNotEqual ""
//
//        val maxResultMap = phMaxJob(panelResult).perform().asInstanceOf[MapArgs]
//        val maxDF = maxResultMap.get("max_calc_action").asInstanceOf[DFArgs].get
//        val maxResultCount = maxDF.count()
//        println(s"${index}-maxResultCount = ${maxResultCount}")
//        maxResultCount mustNotEqual 0
//
//        val finalResult = maxDF.agg(Map("f_sales" -> "sum", "f_units" -> "sum"))
//        val f_sales = finalResult.collect()(0).get(0)
//        val f_units = finalResult.collect()(0).get(1)
//        println(s"${index}-f_sales = ${f_sales}")
//        println(s"${index}-f_units = ${f_units}")
//        f_sales mustNotEqual 0.0
//        f_units mustNotEqual 0.0
//        ~=(f_sales, compareResultSales, 1.0E-3) mustEqual true
//        ~=(f_units, compareResultUnits, 1.0E-3) mustEqual true
//    }
//
//    def ~=(x: Any, y: Any, precision: Double) = {
//        if ((x.asInstanceOf[Double] - y.asInstanceOf[Double]).abs < precision) true else false
//    }
//
//    def doLoopJobs(count: Int): Boolean = {
//        val bool = if(count <= 1 ) true else doLoopJobs(count - 1)
//        doMaxJob(count)
//        bool
//    }
//
//}
//
