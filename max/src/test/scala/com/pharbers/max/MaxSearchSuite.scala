package com.pharbers.max

import com.pharbers.builder.SearchFacade
import com.pharbers.builder.search.SearchSimpleCheck
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
    val user: String = "5afa57a1ed925c05c6f69c68"
    val jobId: String = "20180523test001"
    val ym = "201810"
    val mkt = "麻醉市场"

    test("history search"){

        val args: Map[String, String] = Map(
            "company" -> company,
            "user" -> user,
            "ym_condition" -> "201704-201712",
//            "ym_condition" -> "201801-201802",
            "mkt" -> mkt,
            "pageIndex" -> "1",
            "singlePageSize" -> "10"
        )

        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs]
//        val itemsCount = searchResult.get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get.count()
//        println(s"### => ${itemsCount}")
        val searchResult1 =  searchResult.get("return_page_cache_action").asInstanceOf[ListArgs].get
        println(searchResult1.length)
        searchResult1.foreach(x => println(s"### => ${x}"))
    }

    test("get panel info"){
        val panelInfo = phPanelResultInfo("5b0237b7810c6e0268fe6ff7", "5b023787810c6e0268fe6ff6", "201712", "痛风市场")
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

    test("SimpleCheck search of Facade") {
        val condition = toJson {
            Map(
                "condition" -> toJson(Map(
                    "years" -> toJson("201712"),
                    "market" -> toJson("痛风市场")
                )),
                "user" -> toJson(
                    Map(
                        "user_id" -> toJson("5b0237b7810c6e0268fe6ff7"),
                        "company" -> toJson(Map("company_id" -> toJson("5b023787810c6e0268fe6ff6")))
                    )
                )
            )
        }

        val search = new SearchFacade
        val result = search.searchSimpleCheck(condition)
        println(result._1.get.get("data").get.as[List[JsValue]].length)
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
