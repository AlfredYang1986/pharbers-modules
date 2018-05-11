//package com.pharbers.max
//
//import java.util.Date
//
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//
//import com.pharbers.calc.{phMaxJob, phMaxJobForPfizerCNS_R, phMaxJobForPfizerDVP}
//import com.pharbers.driver.PhRedisDriver
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
//        val result = phMaxJob("1fab79bf-935e-4253-9fa8-567230e5f94c", "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//
//        println("result = " + result)
//        println()
//        println(s"MAX计算结束时间" + dateformat.format(new Date()))
//    }
//
//    test("pfizer calc test") {
//
//        val mkt = "INF"
//        val redisDriver = new PhRedisDriver()
//        // TODO:测试预设
//        redisDriver.addMap("uid", "company", "Pfizer")
//
//        val panelResult = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", s"${mkt}").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
//        println("panelResult = " + panelResult)
//
//        val result = phMaxJob(panelResult.toString, s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
////        val result = phMaxJob(s"${mkt}_panel_1802.csv", s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//
//        println("result = " + result)
//    }
//
//    test("SpecialMarket DVP calc test") {
//        val mkt = "DVP"
//
////        val panelResult = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", s"${mkt}").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
////        println("panelResult = " + panelResult)
////        val result = phMaxJobForPfizerDVP(panelResult.toString, s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//        val result = phMaxJobForPfizerDVP(s"${mkt}_panel_1802.csv", s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//
//        println("result = " + result)
//    }
//
//    test("SpecialMarket CNS_R calc test") {
//        val mkt = "CNS_R"
//
//        val panelResult = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", s"${mkt}").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
//        println("panelResult = " + panelResult)
//
//        val result = phMaxJobForPfizerCNS_R(panelResult.toString, s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//
//        println("result = " + result)
//    }
//
//}
