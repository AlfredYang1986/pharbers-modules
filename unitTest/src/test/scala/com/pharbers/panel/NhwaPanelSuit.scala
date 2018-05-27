//package com.pharbers.panel
//
//import java.util.Date
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//import com.pharbers.pactions.actionbase.MapArgs
//import com.pharbers.panel.nhwa.{phNhwaCalcYMJob, phNhwaPanelJob}
//
//class NhwaPanelSuit extends FunSuite {
//    implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
//
//    test("nhwa calc ym") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"筛选月份开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phNhwaCalcYMJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx").perform().get
//
//        println("result = " + result)
//        println()
//        println(s"筛选月份结束时间" + dateformat.format(new Date()))
//    }
//
//    test("nhwa panel generator") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
//
//        println("result = " + result)
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//    }
//}
