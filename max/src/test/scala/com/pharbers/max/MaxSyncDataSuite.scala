//package com.pharbers.max
//
//import java.util.Base64
//
//import com.pharbers.calc.phMaxScheduleJob
//import com.pharbers.driver.PhRedisDriver
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-5-23.
//  */
//class MaxSyncDataSuite extends FunSuite {
//
//    private val MAX_ADMIN = "jeorch"
//
//    val rd = new PhRedisDriver()
//    rd.addString("MaxAdmin", MAX_ADMIN) //临时测试，作为验证是否为管理员
//
//    test("数据同步第一步"){
//        val rdd2mongoCount = phMaxScheduleJob(MAX_ADMIN).rdd2mongo
//        println(rdd2mongoCount)
//    }
//
//    test("数据同步第二步"){
//        val mongo2rddCount = phMaxScheduleJob(MAX_ADMIN).mongo2rdd
//        println(mongo2rddCount)
//    }
//
//}
