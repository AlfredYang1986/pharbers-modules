//package com.pharbers.max
//
//import com.pharbers.driver.PhRedisDriver
//import com.pharbers.pactions.actionbase.{DFArgs, MapArgs}
//import com.pharbers.search.phSearchMaxJob
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-5-9.
//  */
//class MaxSearchSuite extends FunSuite {
//
//    test("max search"){
//        val uid = "uid"
//        val redisDriver = new PhRedisDriver()
//        // TODO:测试预设
//        redisDriver.addMap(uid, "company", "Pfizer")
//        val searchResult =  phSearchMaxJob(uid, "201802", "INF").perform().asInstanceOf[MapArgs].get("max_search_action").asInstanceOf[DFArgs].get
//        println(s"searchResult.count = ${searchResult.count()}")
//    }
//
//}
