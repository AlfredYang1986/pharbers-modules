package com.pharbers.max

import com.pharbers.builder.SearchFacade
import com.pharbers.pactions.actionbase._
import com.pharbers.search.{phHistorySearchJob, phMaxResultInfo, phPanelResultInfo}
import org.scalatest.FunSuite
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-5-9.
  */
class MaxSearchSuite extends FunSuite {

    val company: String = "5b028f95ed925c2c705b85ba"
    val user: String = "5b028feced925c2c705b85bb"
    val jobId: String = "20180623test001"
    val ym = "201804"
    val mkt = "INF"

    test("history search"){

        val args: Map[String, String] = Map(
            "company" -> company,
            "user" -> user,
            "ym_condition" -> "201804-201804",
            "mkt" -> mkt,
            "pageIndex" -> "1",
            "singlePageSize" -> "10"
        )

        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs]
        val searchResult1 =  searchResult.get("return_page_cache_action").asInstanceOf[ListArgs].get
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
        val maxResultInfo = phMaxResultInfo(company, ym, mkt)

//        println(maxResultInfo.getMaxResultSales)
//        println(maxResultInfo.getCurrCompanySales)

        println(maxResultInfo.getLastYearResultSales)
        println(maxResultInfo.getLastYearCurrCompanySales)
        println(maxResultInfo.getAreaSalesByRange("CITY_SALES",maxResultInfo.lastYearSingleJobKey).length)
        println(maxResultInfo.getAreaSalesByRange("CITY_SALES",maxResultInfo.lastYearSingleJobKey))
        println(maxResultInfo.getCityLstMap)
//        println(maxResultInfo.getLastSeveralMonthResultSalesLst(12))
//        println(maxResultInfo.getLastYearResultSalesPercentage)
//        println(maxResultInfo.getLastYearCurrCompanySalesPercentage)
//        println(maxResultInfo.getLastYearCurrCompanySales)
//        println(maxResultInfo.getCityLstMap.take(10))
//        println(maxResultInfo.getProvLstMap.length)
    }

    test("history search of Facade") {
        val condition = toJson {
            Map(
                "condition" -> toJson(Map(
                    "user_id" -> toJson(user),
                    "startTime" -> toJson("201701"),
                    "endTime" -> toJson("201801"),
                    "currentPage" -> toJson(1),
                    "pageSize" -> toJson(10),
                    "market" -> toJson(mkt)
                )),
                "user" -> toJson(Map("company" -> toJson(Map("company_id" -> toJson(company)))))
            )
        }

        val search = new SearchFacade
        println(search.searchHistory(condition)._1.get.get("data").get.as[List[JsValue]].length)
        println(search.searchHistory(condition)._1.get.get("page").get)
        search.searchHistory(condition)._1.get.get("data").get.as[List[JsValue]].foreach(println)
    }

    test("export data of Facade") {
        val condition = toJson {
            Map(
                "condition" -> toJson(Map(
                    "startTime" -> toJson("201701"),
                    "endTime" -> toJson("201801"),
                    "market" -> toJson(mkt)
                )),
                "user" -> toJson(Map("company" -> toJson(Map("company_id" -> toJson(company)))))
            )
        }

        val search = new SearchFacade
        println(search.exportData(condition)._1.get.get("export_file_name").get)
    }

}
