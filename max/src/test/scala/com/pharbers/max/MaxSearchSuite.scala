//package com.pharbers.max
//
//import com.pharbers.builder.SearchFacade
//import com.pharbers.driver.PhRedisDriver
//import com.pharbers.pactions.actionbase._
//import org.scalatest.FunSuite
//import play.api.libs.json.JsValue
//import play.api.libs.json.Json.toJson
//import com.pharbers.search.{phHistorySearchJob, phMaxResultInfo, phPanelResultInfo}
//
///**
//  * Created by jeorch on 18-5-9.
//  */
//class MaxSearchSuite extends FunSuite {
//
//    val company: String = "5afa53bded925c05c6f69c54"
//    val user: String = "5afa57a1ed925c05c6f69c68"
//    val ym = "201712"
//    val mkt = "麻醉市场"
//
//    test("history search"){
//
//        val args: Map[String, String] = Map(
//            "company" -> company,
//            "user" -> user,
//            "ym_condition" -> "201711-201712",
//            "mkt" -> "麻醉市场1",
//            "pageIndex" -> "0"
//        )
//
//        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs].get("page_search_action").asInstanceOf[ListArgs].get
//
//        println(s"### => ${searchResult.length}")
//        searchResult.foreach(x => println(s"### => ${x}"))
//    }
//
//    test("history search of Facade"){
//        val condition = toJson{
//            Map(
//                "condition" -> toJson(Map(
//                    "user_id" -> toJson(user),
//                    "market" -> toJson(mkt)
//                )),
//                "user" -> toJson(Map("company" -> toJson(Map("company_id" -> toJson(company)))))
//            )
//        }
//
//        val search = new SearchFacade
//        search.searchHistory(condition)._1.get.foreach(x => println(x))
//
//        val search2 = new SearchFacade
//        search2.searchHistory(condition)._1.get.foreach(x => println(x))
//    }
//
//    test("get panel info"){
//        val panelInfo = phPanelResultInfo(user, company, ym, mkt)
//        println(panelInfo.getHospCount)
//        println(panelInfo.getProdCount)
//        println(panelInfo.getPanelSales)
//        println(panelInfo.getCurrCompanySales)
//        println(panelInfo.getCurrCompanyShare)
//        println(panelInfo.getNotPanelHospLst.length)
//        println(panelInfo.getNotPanelHospLst.take(10))
//    }
//
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
//    test("get simple check"){
//        val rd = new PhRedisDriver()
//        val tmp = phPanelResultInfo(user, company, ym, mkt)
//        val tmpMax = phMaxResultInfo(user, company, ym, mkt)
////        tmp.setValue2Array(1, "20").foreach(println)// 柱状图
////        tmp.baseLine("HOSP_ID").foreach(println) // 基准线
//
////        val a = rd.getMapValue("c2e877bdd44a63036689a78c5312661c", "max_sales").toDouble
//        val a = tmpMax.getMaxResultSales
//        println(a)
//    }
//}
