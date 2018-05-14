package com.pharbers.search

import com.pharbers.pactions.actionbase.{DFArgs, ListArgs, MapArgs, StringArgs}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait searchTrait {

    private def getAllMkt(company_id: String): JsValue = {
        toJson( "mkt1" :: "mkt2" :: Nil )
    }

    def searchAllMkt(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get
        val temp = Some(
            Map(
                "markets" -> getAllMkt(company_id)
            )
        )

        (temp, None)
    }

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

    //TODO: get singleJob's calcYM from redis
    def searchSimpleCheckSelect(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val job_id = (jv \ "condition" \ "job_id").asOpt[String].get
        val user_id = (jv \ "user" \ "user_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get
        val temp = Some(
            Map(
                "years" -> toJson(
                    "201701" :: "201702" :: Nil
                ),
                "markets" -> getAllMkt(company_id)
            )
        )

        (temp, None)
    }

    def searchSimpleCheck(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val job_id = (jv \ "condition" \ "job_id").asOpt[String].get
        val market = (jv \ "condition" \ "market").asOpt[String].get
        val years = (jv \ "condition" \ "years").asOpt[String].get
        val user_id = (jv \ "user" \ "user_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get

        //TODO:get ym from jv
        val ym = (jv \ "condition" \ "ym").asOpt[String].get
        val panelInfo = phPanelResultInfo(user_id, company_id, ym, market)

        val temp = Some(
            Map(
                "hospital" -> toJson(
                    Map(
                        "baselines" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "samplenumbers" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "currentNumber" -> toJson(panelInfo.getHospCount),
                        "lastYearNumber" -> toJson("234")
                    )
                ),
                "product" -> toJson(
                    Map(
                        "baselines" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "samplenumbers" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "currentNumber" -> toJson(panelInfo.getProdCount),
                        "lastYearNumber" -> toJson("234")
                    )
                ),
                "sales" -> toJson(
                    Map(
                        "baselines" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "samplenumbers" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "currentNumber" -> toJson(panelInfo.getPanelSales),
                        "lastYearNumber" -> toJson("234")
                    )
                ),
                "notfindhospital" -> toJson(panelInfo.getNotPanelHospLst.zipWithIndex.map(x => {
                    val temp = x._1.replace("[", "").replace("]", "").split(",")
                    toJson(Map(
                        "index" -> toJson(x._2),
                        "hospitalName" -> toJson(temp(0)),
                        "province" -> toJson(temp(1)),
                        "city" -> toJson(temp(2)),
                        "cityLevel" -> toJson(temp(3))
                    ))
                }))
            )
        )

        (temp, None)
    }

    //TODO:0515 do this
    def searchResultCheck(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val job_id = (jv \ "condition" \ "job_id").asOpt[String].get
        val market = (jv \ "condition" \ "market").asOpt[String].get
        val years = (jv \ "condition" \ "years").asOpt[String].get
        val user_id = (jv \ "user" \ "user_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get

        val temp = Some(
            Map(
                "indicators" -> toJson(
                    Map(
                        "marketSumSales" -> toJson(
                            Map(
                                "currentNumber" -> toJson("123.11"),
                                "lastYearPercentage" -> toJson("3.12")
                            )
                        ),
                        "productSales" -> toJson(
                            Map(
                                "currentNumber" -> toJson("123.11"),
                                "lastYearPercentage" -> toJson("3.12")
                            )
                        )
                    )
                ),
                "trend" -> toJson(
                    Map(
                        "date" -> toJson("201701"),
                        "percentage" -> toJson("12.1"),
                        "marketSales" -> toJson("1000")
                    )
                            :: Map(
                        "date" -> toJson("201612"),
                        "percentage" -> toJson("13.1"),
                        "marketSales" -> toJson("100")
                    ) :: Nil
                ),
                "region" -> toJson(
                    Map(
                        "name" -> toJson("北京"),
                        "value" -> toJson("111"),
                        "prodcutSales" -> toJson("12"),
                        "percentage" -> toJson("1.1")
                    ) :: Map(
                        "name" -> toJson("天津"),
                        "value" -> toJson("111"),
                        "prodcutSales" -> toJson("12"),
                        "percentage" -> toJson("1.1")
                    ) :: Map(
                        "name" -> toJson("上海"),
                        "value" -> toJson("111"),
                        "prodcutSales" -> toJson("12"),
                        "percentage" -> toJson("1.1")
                    ) :: Nil
                ),
                "mirror" -> toJson(
                    Map(
                        "provinces" -> toJson(
                            Map(
                                "current" -> toJson(
                                    Map(
                                        "province" -> toJson("北京市"),
                                        "marketSales" -> toJson("1111"),
                                        "percentage" -> toJson("1.1")
                                    ) :: Map(
                                        "province" -> toJson("天津市"),
                                        "marketSales" -> toJson("1111"),
                                        "percentage" -> toJson("1.1")
                                    ) :: Nil
                                ),
                                "lastyear" -> toJson(
                                    Map(
                                        "province" -> toJson("北京市"),
                                        "marketSales" -> toJson("1111"),
                                        "percentage" -> toJson("1.1")
                                    ) :: Map(
                                        "province" -> toJson("天津市"),
                                        "marketSales" -> toJson("1111"),
                                        "percentage" -> toJson("1.1")
                                    ) :: Nil
                                )
                            )
                        ),
                        "city" -> toJson(
                            Map(
                                "current" -> toJson(
                                    Map(
                                        "city" -> toJson("北京市"),
                                        "marketSales" -> toJson("1111"),
                                        "percentage" -> toJson("1.1")
                                    ) :: Map(
                                        "city" -> toJson("天津市"),
                                        "marketSales" -> toJson("1111"),
                                        "percentage" -> toJson("1.1")
                                    ) :: Nil
                                ),
                                "lastyear" -> toJson(
                                    Map(
                                        "city" -> toJson("北京市"),
                                        "marketSales" -> toJson("1111"),
                                        "percentage" -> toJson("1.1")
                                    ) :: Map(
                                        "city" -> toJson("天津市"),
                                        "marketSales" -> toJson("1111"),
                                        "percentage" -> toJson("1.1")
                                    ) :: Nil
                                )
                            )
                        )
                    )
                )
            )
        )

        (temp, None)
    }

}
