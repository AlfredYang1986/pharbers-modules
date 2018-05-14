package com.pharbers.builder.search

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.search.phHistorySearchJob
import com.pharbers.pactions.actionbase.{DFArgs, ListArgs, MapArgs, StringArgs}

trait SearchHistory {
    def searchHistory(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val user = (jv \ "condition" \ "user_id").asOpt[String].getOrElse(throw new Exception("Illegal user"))
        val company = (jv \ "condition" \ "company").asOpt[String].getOrElse(throw new Exception("Illegal company"))
        val market = (jv \ "condition" \ "market").asOpt[String].getOrElse("")
        val startTime = (jv \ "condition" \ "startTime").asOpt[String].getOrElse("")
        val endTime = (jv \ "condition" \ "endTime").asOpt[String].getOrElse("")
        val currentPage = (jv \ "condition" \ "currentPage").asOpt[String].getOrElse("0")
        val pageSize = (jv \ "condition" \ "pageSize").asOpt[String].getOrElse("20")

        val args: Map[String, String] = Map(
            "company" -> company,
            "user" -> user,
            "pageIndex" -> currentPage,
            "singlePageSize" -> pageSize,
            "ym_condition" -> s"${startTime}-${endTime}",
            "mkt" -> market
        )

        val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs]

        val itemsCount = searchResult.get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get.count()
        val pagesCount = itemsCount/pageSize.toLong

        val singlePageData = searchResult.get("page_search_action").asInstanceOf[ListArgs].get.zipWithIndex.map(x => {
            val item = x._1.asInstanceOf[StringArgs].get.replace("[","").replace("]","").split(",")
            toJson(
                Map(
                    "id" -> toJson(x._2),
                    "type" -> toJson("dataCenter"),
                    "attributes" -> toJson(
                        Map(
                            "date" -> toJson(item(0)),
                            "province" -> toJson(item(2)),
                            "market" -> toJson(item(8)),
                            "product" -> toJson(item(4)),
                            "sales" -> toJson(item(7)),
                            "units" -> toJson(item(6))
                        )
                    )
                )
            )
        })

        val temp = Some(
            Map(
                "data" -> toJson(singlePageData),
                "page" -> toJson(
                    Map(
                        "itemsCount" -> toJson(itemsCount),
                        "pagesCount" -> toJson(pagesCount)
                    )
                )
            )
        )

        (temp, None)
    }
}