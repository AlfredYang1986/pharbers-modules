package com.pharbers.builder.search

import com.pharbers.driver.PhRedisDriver
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.search.phHistorySearchJob
import com.pharbers.pactions.actionbase.{DFArgs, ListArgs, MapArgs, StringArgs}
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver

trait SearchHistory {
    def searchHistory(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val user = (jv \ "condition" \ "user_id").asOpt[String].getOrElse(throw new Exception("Illegal user"))
        val company = (jv \ "user" \ "company" \ "company_id").asOpt[String].getOrElse(throw new Exception("Illegal company"))
        val market = (jv \ "condition" \ "market").asOpt[String].getOrElse("")
        val startTime = (jv \ "condition" \ "startTime").asOpt[String].getOrElse("")
        val endTime = (jv \ "condition" \ "endTime").asOpt[String].getOrElse("")
        val currentPage = (jv \ "condition" \ "currentPage").asOpt[Int].getOrElse(1) match{
            case i: Int if i < 1 => "0"
            case i => (i - 1).toString
        }
        val pageSize = (jv \ "condition" \ "pageSize").asOpt[Int].getOrElse(20)
        val mode = (jv \ "condition" \ "mode").asOpt[String].getOrElse("search")

        val args: Map[String, String] = Map(
            "company" -> company,
            "user" -> user,
            "pageIndex" -> currentPage.toString,
            "singlePageSize" -> pageSize.toString,
            "ym_condition" -> s"$startTime-$endTime",
            "mkt" -> market
        )

        val redisDriver = new PhRedisDriver()
        val userJobsKey = Sercurity.md5Hash(user + company)
        val history_df_lst = redisDriver.getSetAllValue(userJobsKey)
        val (singlePageData, itemsCount, pagesCount) = if(history_df_lst.nonEmpty) {
            val searchResult =  phHistorySearchJob(args).perform().asInstanceOf[MapArgs]
            val singleSearchKey = Sercurity.md5Hash(user + company + startTime + endTime + market + pageSize)
            //TODO: 历史数据搜索时，一次条件搜索就需要重新计算count，暂时为了分页搜索时的效率，临时缓存30min的条件搜索的count
            val itemsCount = mode match {
                case "page" => redisDriver.getString(singleSearchKey).toLong
                case _ =>
                    val count = searchResult.get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get.count()
                    redisDriver.addString(singleSearchKey, count.toString)
                    redisDriver.expire(singleSearchKey, 60*30)   //过期时间30min
                    count
            }
            val pagesCount = itemsCount/pageSize.toLong

            val singlePageData = searchResult.get("page_search_action").asInstanceOf[ListArgs].get.zipWithIndex.map(x => {
                val item = x._1.asInstanceOf[StringArgs].get.replace("[","").replace("]","").split(",")
                toJson(
                    Map(
                        "id" -> toJson(x._2 + 1),
                        "type" -> toJson("dataCenter"),
                        "attributes" -> toJson(
                            Map(
                                "date" -> toJson(item(0)),
                                "province" -> toJson(item(1)),
                                "city" -> toJson(item(2)),
                                "market" -> toJson(item(8)),
                                "product" -> toJson(item(4)),
                                "sales" -> toJson(item(7)),
                                "units" -> toJson(item(6))
                            )
                        )
                    )
                )
            })

            phSparkDriver().sc.stop
            (singlePageData, itemsCount, pagesCount)
        } else {
            (Nil, 0L, 0L)
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
