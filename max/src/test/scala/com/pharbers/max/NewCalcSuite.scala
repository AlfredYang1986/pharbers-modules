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
//class NewCalcSuite extends FunSuite{
//    implicit def progressFunc(progress: Double, flag: String): Unit = Unit
//
//    test("nhwa calc test") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"MAX计算开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phMaxJob("00_luo_00/Nhwa_Panel_1712.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
//
//        println("result = " + result)
//        println()
//        println(s"MAX计算结束时间" + dateformat.format(new Date()))
//    }
//
//}
