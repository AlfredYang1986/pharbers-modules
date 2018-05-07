//package com.pharbers.panel
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.pharbers.pactions.actionbase.MapArgs
//import com.pharbers.panel.pfizer.phPfizerPanelJob
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-4-19.
//  */
//class PfizerPanelSuite extends FunSuite {
//
//    test("pfizer generate panel"){
//        val mkt = "DVP"
//
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", s"${mkt}").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
//        println("result = " + result)
//
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//    }
//
//}
