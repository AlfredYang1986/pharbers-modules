package com.pharbers.max

import com.pharbers.builder.SearchFacade
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.search.{phHistorySearchJob, phMaxResultInfo, phPanelResultInfo}
import org.scalatest.FunSuite
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-5-9.
  */
class MaxSearchSuite extends FunSuite {

    val company: String = "5afa53bded925c05c6f69c54"
    val user: String = "5afaa333ed925c30f8c066d1"
    val ym = "201712"
    val mkt = "麻醉市场"

    test("history search"){

        val args: Map[String, String] = Map(
            "company" -> company,
            "user" -> user,
            "ym_condition" -> "201711-201712",
//            "ym_condition" -> "201801-201802",
            "mkt" -> "麻醉市场",
            "pageIndex" -> "6",
            "singlePageSize" -> "10"
        )

        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs]
//        val itemsCount = searchResult.get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get.count()
//        println(s"### => ${itemsCount}")
        val searchResult1 =  searchResult.get("page_search_action").asInstanceOf[ListArgs].get
        println(searchResult1.length)
        searchResult1.foreach(x => println(s"### => ${x}"))
    }

    test("get panel info"){
        val panelInfo = phPanelResultInfo(user, company, ym, mkt)
        println(panelInfo.getHospCount)
        println(panelInfo.getProdCount)
        println(panelInfo.getPanelSales)
        println(panelInfo.getCurrCompanySales)
        println(panelInfo.getCurrCompanyShare)
        println(panelInfo.getNotPanelHospLst)
        println(panelInfo.getNotPanelHospLst.take(10))
    }
    test("get max info"){
        val maxResultInfo = phMaxResultInfo(user, company, ym, mkt)
        println(maxResultInfo.getLastSeveralMonthResultSalesLst(12))
        println(maxResultInfo.getMaxResultSales)
        println(maxResultInfo.getLastYearResultSales)
        println(maxResultInfo.getCurrCompanySales)
        println(maxResultInfo.getLastYearCurrCompanySales)
        println(maxResultInfo.getCityLstMap.take(10))
        println(maxResultInfo.getProvLstMap.length)
    }

    test("history search of Facade") {
        val condition = toJson {
            Map(
                "condition" -> toJson(Map(
                    "user_id" -> toJson(user),
                    "startTime" -> toJson("201712"),
                    "endTime" -> toJson("201712"),
                    "currentPage" -> toJson(72506),
                    "pageSize" -> toJson(10),
                    "mode" -> toJson("search"),
                    "market" -> toJson("All")
                )),
                "user" -> toJson(Map("company" -> toJson(Map("company_id" -> toJson(company)))))
            )
        }

        val search = new SearchFacade
        search.searchHistory(condition)._1.get.get("data").get.as[List[JsValue]].foreach(println)
    }

}
