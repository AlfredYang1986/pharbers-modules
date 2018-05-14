//package com.pharbers.max
//
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
//        val company: String = "恩华"
//        val user: String = "testUser"
//
//        val args: Map[String, String] = Map(
//            "company" -> company,
//            "user" -> user,
//            "pageIndex" -> "2"
//        )
//
//        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs].get("page_search_action").asInstanceOf[ListArgs].get
//
//        println(s"### => ${searchResult.length}")
//        println(s"### => ${searchResult}")
//    }
//
//}
