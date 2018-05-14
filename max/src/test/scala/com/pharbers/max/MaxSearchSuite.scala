//package com.pharbers.max
//
//import com.pharbers.driver.PhRedisDriver
//import com.pharbers.pactions.actionbase._
//import com.pharbers.search.phHistorySearchJob
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-5-9.
//  */
//class MaxSearchSuite extends FunSuite {
//
//    test("history search"){
//        val uid = "uid"
//        val redisDriver = new PhRedisDriver()
//        // TODO:测试预设
//        redisDriver.addMap(uid, "company", "Pfizer")
//        val searchResult =  phHistorySearchJob(uid, 0, 20).perform().asInstanceOf[MapArgs].get("page_search_action").asInstanceOf[ListArgs].get
//
//        println(s"### => ${searchResult.length}")
//        println(s"### => ${searchResult}")
//    }
//
//}
