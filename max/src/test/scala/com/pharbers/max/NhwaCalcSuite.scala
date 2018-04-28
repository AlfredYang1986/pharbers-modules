//package com.pharbers.max
//
//import java.util.Date
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//import com.pharbers.calc.phMaxJob
//import com.pharbers.pactions.actionbase.MapArgs
//
///**
//  * Created by jeorch on 18-4-10.
//  */
//class NhwaCalcSuite extends FunSuite{
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
//}
