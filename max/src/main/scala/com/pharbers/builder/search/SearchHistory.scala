package com.pharbers.builder.search

import com.pharbers.driver.PhRedisDriver
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.search.phHistorySearchJob
import com.pharbers.pactions.actionbase.{ListArgs, MapArgs, StringArgs}
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver

trait SearchHistory {
    def searchHistory(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val user = (jv \ "condition" \ "user_id").asOpt[String].getOrElse(throw new Exception("Illegal user"))
        val company = (jv \ "user" \ "company" \ "company_id").asOpt[String].getOrElse(throw new Exception("Illegal company"))
        val market = (jv \ "condition" \ "market").asOpt[String].getOrElse("")
        val startTime = (jv \ "condition" \ "startTime").asOpt[String].getOrElse("")
        val endTime = (jv \ "condition" \ "endTime").asOpt[String].getOrElse("")
        val pageSize = (jv \ "condition" \ "pageSize").asOpt[Int].getOrElse(20)
        val currentPage = (jv \ "condition" \ "currentPage").asOpt[Int].getOrElse(1) match{
            case i: Int if i < 1 => "0"
            case i => (i - 1).toString
        }

        val args: Map[String, String] = Map(
            "company" -> company,
            "user" -> user,
            "pageIndex" -> currentPage.toString,
            "singlePageSize" -> pageSize.toString,
            "ym_condition" -> s"$startTime-$endTime",
            "mkt" -> market
        )

        val redisDriver = new PhRedisDriver()

        // 查询缓存
        val searchResult = phHistorySearchJob(args).perform().asInstanceOf[MapArgs]
        phSparkDriver().sc.stop

        val pageCacheInfo = Sercurity.md5Hash(user + company + args("ym_condition") + args("mkt"))
        val itemsCount = redisDriver.getMapValue(pageCacheInfo, "count")
        val pagesCount = redisDriver.getMapValue(pageCacheInfo, "page")


        val singlePageData = searchResult.get("return_page_cache_action").asInstanceOf[ListArgs].get.zipWithIndex.map { x =>
            val item = x._1.asInstanceOf[StringArgs].get.replace("[", "").replace("]", "").split(",")
            toJson(
                Map(
                    "id" -> toJson(x._2 + 1),
                    "type" -> toJson("dataCenter"),
                    "attributes" -> toJson(
                        Map(
                            "date" -> toJson(item(0)),
                            "province" -> toJson(item(1)),
                            "city" -> toJson(item(2)),
                            "market" -> toJson(item(3)),
                            "product" -> toJson(item(4)),
                            "sales" -> toJson(item(5)),
                            "units" -> toJson(item(6))
                        )
                    )
                )
            )
        }

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
