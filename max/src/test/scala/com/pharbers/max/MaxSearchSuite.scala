//package com.pharbers.max
//
//import com.pharbers.driver.PhRedisDriver
//import com.pharbers.pactions.actionbase._
//import com.pharbers.search.{phPanelSearchJob, phResultSearchJob}
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-5-9.
//  */
//class MaxSearchSuite extends FunSuite {
//
//    test("result search"){
//        val uid = "uid"
//        val redisDriver = new PhRedisDriver()
//        // TODO:测试预设
//        redisDriver.addMap(uid, "company", "Pfizer")
//        val searchResult =  phResultSearchJob(uid, "201802", "INF", 0, 20).perform().asInstanceOf[MapArgs].get("page_search_action").asInstanceOf[ListArgs].get
//
//        println(s"### => END")
//        println(s"### => ${searchResult.length}")
//        println(s"### => ${searchResult}")
//    }
//
//    test("panel search"){
//        val uid = "uid"
//        val redisDriver = new PhRedisDriver()
//        // TODO:测试预设
//        redisDriver.addMap(uid, "company", "Pfizer")
//        val searchResult =  phPanelSearchJob(uid, "201802", "INF", 0, 20).perform().asInstanceOf[MapArgs].get("page_search_action").asInstanceOf[ListArgs].get
//
//        println(s"### => END")
//        println(s"### => ${searchResult.length}")
//        println(s"### => ${searchResult}")
//    }
//
//    test("history search"){
//        
//    }
//
//}
