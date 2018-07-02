//package com.pharbers.max
//
//import com.pharbers.pactions.actionbase.{MapArgs, StringArgs}
//import com.pharbers.search.phExportSearchDataJob
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-6-5.
//  */
//class MaxExportSuite extends FunSuite {
//
//    val company: String = "5afa53bded925c05c6f69c54"
//    val user: String = "5afa57a1ed925c05c6f69c68"
//    val jobId: String = "20180523test001"
//    val ym = "201810"
//    val mkt = "麻醉市场"
//
//    test("export search result"){
//
//        val args: Map[String, String] = Map(
//            "company" -> company,
//            "ym_condition" -> "201704-201712",
//            "mkt" -> mkt
//        )
//
//        val exportResult =  phExportSearchDataJob(args).perform().asInstanceOf[MapArgs].get("export_search_data_action").asInstanceOf[StringArgs].get
//        println(exportResult)
//    }
//
//}
