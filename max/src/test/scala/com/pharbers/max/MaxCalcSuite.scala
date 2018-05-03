//package com.pharbers.max
//
//import java.util.Date
//
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//
//import com.pharbers.calc.{phMaxJob, phMaxJobForPfizerCNS_R, phMaxJobForPfizerDVP}
//import com.pharbers.pactions.actionbase.MapArgs
//import com.pharbers.panel.pfizer.phPfizerPanelJob
//
///**
//  * Created by jeorch on 18-4-10.
//  */
//class MaxCalcSuite extends FunSuite{
//    implicit def progressFunc(progress: Double, flag: String): Unit = Unit
//
//    test("nhwa calc test") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"MAX计算开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phMaxJob("1fab79bf-935e-4253-9fa8-567230e5f94c", "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
//
//        println("result = " + result)
//        println()
//        println(s"MAX计算结束时间" + dateformat.format(new Date()))
//    }
//
//    test("pfizer calc test") {
//
//        val mkt = "INF"
//
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        val panelResult = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", s"${mkt}").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
//        println("panelResult = " + panelResult)
//
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//
//        println(s"MAX计算开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phMaxJob(panelResult.toString, s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
//
//        println("result = " + result)
//        println()
//        println(s"MAX计算结束时间" + dateformat.format(new Date()))
//    }
//
//    test("SpecialMarket DVP calc test") {
//        val mkt = "DVP"
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"MAX计算开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phMaxJobForPfizerDVP(s"CPA_GYCX_panel_201802${mkt}.xlsx", s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
//
//        println("result = " + result)
//        println()
//        println(s"MAX计算结束时间" + dateformat.format(new Date()))
//    }
//
//    test("SpecialMarket CNS_R calc test") {
//        val mkt = "CNS_R"
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"MAX计算开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phMaxJobForPfizerCNS_R(s"${mkt}_panel", s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
//
//        println("result = " + result)
//        println()
//        println(s"MAX计算结束时间" + dateformat.format(new Date()))
//    }
//
//}
