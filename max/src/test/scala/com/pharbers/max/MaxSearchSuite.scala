//package com.pharbers.max
//
//import com.pharbers.builder.SearchFacade
//import com.pharbers.pactions.actionbase._
//import com.pharbers.search.{phHistorySearchJob, phMaxResultInfo, phPanelResultInfo}
//import org.scalatest.FunSuite
//import play.api.libs.json.JsValue
//import play.api.libs.json.Json.toJson
//
///**
//  * Created by jeorch on 18-5-9.
//  */
//class MaxSearchSuite extends FunSuite {
//
//    val company: String = "5b028f95ed925c2c705b85ba"
//    val user: String = "5b028feced925c2c705b85bb"
//    val jobId: String = "20180523test001"
//    val ym = "201802"
//    val mkt = "INF"
//
//    test("history search"){
//
//        val args: Map[String, String] = Map(
//            "company" -> company,
//            "user" -> user,
//            "ym_condition" -> "201711-201712",
////            "ym_condition" -> "201801-201802",
//            "mkt" -> mkt,
//            "pageIndex" -> "6",
//            "singlePageSize" -> "10"
//        )
//
//        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs]
////        val itemsCount = searchResult.get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get.count()
////        println(s"### => ${itemsCount}")
//        val searchResult1 =  searchResult.get("page_search_action").asInstanceOf[ListArgs].get
//        println(searchResult1.length)
//        searchResult1.foreach(x => println(s"### => ${x}"))
//    }
//
//    test("get panel info"){
//        val panelInfo = phPanelResultInfo(user, company, ym, mkt)
//        println(panelInfo.getHospCount)
//        println(panelInfo.getProdCount)
//        println(panelInfo.getPanelSales)
//        println(panelInfo.getCurrCompanySales)
//        println(panelInfo.getCurrCompanyShare)
//        println(panelInfo.getNotPanelHospLst)
//        println(panelInfo.getNotPanelHospLst.take(10))
//    }
//
//    test("get max info"){
//        val maxResultInfo = phMaxResultInfo(user, company, ym, mkt)
//        println(maxResultInfo.getLastSeveralMonthResultSalesLst(12))
//        println(maxResultInfo.getMaxResultSales)
//        println(maxResultInfo.getLastYearResultSales)
//        println(maxResultInfo.getCurrCompanySales)
//        println(maxResultInfo.getLastYearCurrCompanySales)
//        println(maxResultInfo.getCityLstMap.take(10))
//        println(maxResultInfo.getProvLstMap.length)
//    }
//
//    test("history search of Facade") {
//        val condition = toJson {
//            Map(
//                "condition" -> toJson(Map(
//                    "user_id" -> toJson(user),
//                    "startTime" -> toJson("201801"),
//                    "endTime" -> toJson("201802"),
//                    "currentPage" -> toJson(1),
//                    "pageSize" -> toJson(10),
//                    "market" -> toJson(mkt)
//                )),
//                "user" -> toJson(Map("company" -> toJson(Map("company_id" -> toJson(company)))))
//            )
//        }
//
//        val search = new SearchFacade
//        println(search.searchHistory(condition)._1.get.get("data").get.as[List[JsValue]].length)
//        search.searchHistory(condition)._1.get.get("data").get.as[List[JsValue]].foreach(println)
//    }
//
//}
