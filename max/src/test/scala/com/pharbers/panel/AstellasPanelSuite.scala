//package com.pharbers.panel
//
//import java.util.Date
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//import com.pharbers.pactions.actionbase.MapArgs
//import com.pharbers.panel.astellas.{phAstellasCalcYMJob, phAstellasPanelJob}
//
//class AstellasPanelSuite extends FunSuite {
//    implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
//
//    test("astellas calc ym") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"筛选月份开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phAstellasCalcYMJob("/mnt/config/Client/astl_cpa-10.xlsx", "/mnt/config/Client/astl_gycx_1-10.xlsx").perform().asInstanceOf[MapArgs].get("result").get
//
//        println("result = " + result)
//        println()
//        println(s"筛选月份结束时间" + dateformat.format(new Date()))
//    }
//
//    test("astellas panel generator") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phAstellasPanelJob("/mnt/config/Client/astl_cpa-10.xlsx", "/mnt/config/Client/astl_gycx_1-10.xlsx", "201710", "阿洛刻市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
//
//        println("result = " + result)
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//    }
//
//}
