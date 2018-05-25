//package com.pharbers.max
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.pharbers.builder.SearchFacade
//import com.pharbers.calc.{phMaxJob, phMaxJobForPfizerCNS_R}
//import com.pharbers.pactions.actionbase.{DFArgs, MapArgs}
//import com.pharbers.panel.astellas.phAstellasPanelJob
//import com.pharbers.panel.nhwa.phNhwaPanelJob
//import com.pharbers.panel.pfizer.phPfizerPanelJob
//import com.pharbers.spark.phSparkDriver
//import org.specs2.mutable.{Before, Specification}
//import org.specs2.specification.BeforeAll
//import org.specs2.specification.core.{Fragment, Fragments}
//import play.api.libs.json.JsValue
//import play.api.libs.json.Json.toJson
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
//    val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//
//    override def beforeAll: Unit = {
//        println("Do beforeAll")
//        //Nhwa
////        val compareResultDF = phSparkDriver().csv2RDD(panel_path_obj.p_resultPath + "00_luo_00/nhwa_max/Nhwa_Max_1712.csv", ",")
//        //Astellas 201712-Allelock
////        val compareResultDF = phSparkDriver().mongo2RDD("127.0.0.1","27017","Max_Test","Allelock_Factorized_Units&Sales_WITH_OT1712").toDF()
////        compareResultCount = compareResultDF.count()
////        val compareResultSum = compareResultDF.agg(Map("f_sales" -> "sum", "f_units" -> "sum")).collect()
////        compareResultSales = compareResultSum(0).get(0).asInstanceOf[Double]
////        compareResultUnits = compareResultSum(0).get(1).asInstanceOf[Double]
//        println(s"compareResultCount = ${compareResultCount}")
//        println(s"compareResultSales = ${compareResultSales}")
//        println(s"compareResultUnits = ${compareResultUnits}")
//    }
//
//    /**
//      * 串行压力测试，每次同时只进行一个Job.
//      */
//    override def is =
//        s2"""
//    This is a specification to check the max result correctness
//    The max result should
//        successfully pass max calc                                               ${doLoopJobs(1000)}
//                                                                 """
//
////    /**
////      *  并发压力测试，每次同时进行的Job数等于Spark机器核心数.
////      */
////    (1 to 20).foldLeft(Fragments.empty)((res, i) => res.append("test "+i ! {
////        doMaxJob(i)
////    }))
//
//    def doLoopJobs(count: Int): Boolean = {
//        val bool = if(count <= 1 ) true else doLoopJobs(count - 1)
////        doMaxJob(count)
//        doSearchJob(count)
//        bool
//    }
//
//    def doMaxJob(index: Int) = {
//        println(s"Do MaxJob-${index}")
////        val panelResult = phAstellasPanelJob("/mnt/config/Client/astl_cpa-12.xlsx", "/mnt/config/Client/astl_gycx_1-12.xlsx", "201712", "Allelock").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//        val panelResult = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", "CNS_R").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//        println(s"${index}-panelResult = ${panelResult}")
//        panelResult mustNotEqual ""
//
////        val maxResultMap = phMaxJob(panelResult, "astellas/UNIVERSE_Allelock_online.xlsx").perform().asInstanceOf[MapArgs]
////        val maxResultMap = phMaxJob("00_luo_00/Allelock_panel1712.xlsx", "astellas/UNIVERSE_Allelock_online.xlsx").perform().asInstanceOf[MapArgs]
//        val maxResultMap = phMaxJobForPfizerCNS_R(panelResult.toString, "pfizer/universe_CNS_R_online.xlsx").perform().asInstanceOf[MapArgs]
//
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
//        //        ~=(f_sales, compareResultSales, 1.0E-3) mustEqual true
//        //        ~=(f_units, compareResultUnits, 1.0E-3) mustEqual true
//    }
//
//    def doSearchJob(index: Int) = {
//        println(s"doSearchJob-${index}")
//        val company: String = "5afa53bded925c05c6f69c54"
//        val user: String = "5afa57a1ed925c05c6f69c68"
//        val ym = "201712"
//        val mkt = "麻醉市场"
//        val condition = toJson {
//            Map(
//                "condition" -> toJson(Map(
//                    "user_id" -> toJson(user),
//                    "currentPage" -> toJson(index),
//                    "pageSize" -> toJson(20),
//                    "market" -> toJson(mkt)
//                )),
//                "user" -> toJson(Map("company" -> toJson(Map("company_id" -> toJson(company)))))
//            )
//        }
//
//        val search = new SearchFacade
//        search.searchHistory(condition)._1.get.get("data").get.as[List[JsValue]].foreach(println)
//    }
//
//    def ~=(x: Any, y: Any, precision: Double) = {
//        if ((x.asInstanceOf[Double] - y.asInstanceOf[Double]).abs < precision) true else false
//    }
//
//}
