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
//    val company: String = "5afa53bded925c05c6f69c54"
//    val user: String = "5afa57a1ed925c05c6f69c68"
//
//    test("history search"){
//
//        val args: Map[String, String] = Map(
//            "company" -> company,
//            "user" -> user,
//            "ym_condition" -> "201711-201712",
////            "ym_condition" -> "201801-201802",
//            "mkt" -> "麻醉市场1",
//            "pageIndex" -> "0"
//        )
//
//        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs]
//        val itemsCount = searchResult.get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get.count()
//
//        val searchResult1 =  searchResult.get("page_search_action").asInstanceOf[ListArgs].get
//
//        println(s"### => ${itemsCount}")
//        searchResult1.foreach(x => println(s"### => ${x}"))
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
