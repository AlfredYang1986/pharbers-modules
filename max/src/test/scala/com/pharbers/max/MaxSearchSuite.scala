//package com.pharbers.max
//
//import com.pharbers.pactions.actionbase._
//import com.pharbers.search.{phHistorySearchJob, phMaxResultInfo, phPanelResultInfo}
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-5-9.
//  */
//class MaxSearchSuite extends FunSuite {
//
//    val company: String = "恩华"
//    val user: String = "testUser"
//
//    test("history search"){
//
//        val args: Map[String, String] = Map(
//            "company" -> company,
//            "user" -> user,
//            "pageIndex" -> "0"
//        )
//
//        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs].get("page_search_action").asInstanceOf[ListArgs].get
//
//        println(s"### => ${searchResult.length}")
//        println(s"### => ${searchResult}")
//    }
//
//    test("get panel info"){
//        val ym = "201712"
//        val mkt = "麻醉市场"
//        val panelInfo = phPanelResultInfo(user, company, ym, mkt)
//        println(panelInfo.getHospCount)
//        println(panelInfo.getProdCount)
//        println(panelInfo.getPanelSales)
//        println(panelInfo.getCurrCompanySales)
//        println(panelInfo.getCurrCompanyShare)
//        println(panelInfo.getNotPanelHospLst.length)
//        println(panelInfo.getNotPanelHospLst.take(10))
//    }
//    test("get max info"){
//        val ym = "201712"
//        val mkt = "麻醉市场"
//        val maxResultInfo = phMaxResultInfo(user, company, ym, mkt)
//        println(maxResultInfo.getLastSeveralMonthResultSalesLst(12))
//        println(maxResultInfo.getMaxResultSales)
//        println(maxResultInfo.getLastYearResultSales)
//        println(maxResultInfo.getCurrCompanySales)
//        println(maxResultInfo.getLastYearCurrCompanySales)
//        println(maxResultInfo.getCityLstMap.take(10))
//        println(maxResultInfo.getProvLstMap.take(10))
//    }
//
//}
