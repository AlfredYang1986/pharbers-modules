//package com.pharbers.max
//
//import com.pharbers.driver.PhRedisDriver
//import com.pharbers.pactions.actionbase._
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
//        val searchResult =  phSearchMaxJob(uid, "201802", "INF", 0, 20).perform().asInstanceOf[MapArgs].get("max_search_action").asInstanceOf[ListArgs].get
//
//        println(s"### => END")
//        println(s"### => ${searchResult.length}")
//        println(s"### => ${searchResult}")
//    }
//
//}
