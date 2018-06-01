package com.pharbers.builder.search

import com.pharbers.search.{phMaxResultInfo, phMaxSearchTrait}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait SearchResultCheck extends phMaxSearchTrait {

    def searchResultCheck(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val job_id = (jv \ "condition" \ "job_id").asOpt[String].get
        val market = (jv \ "condition" \ "market").asOpt[String].get
        val years = (jv \ "condition" \ "years").asOpt[String].get
        val user_id = (jv \ "user" \ "user_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get

        val max = phMaxResultInfo(company_id, years, market)

        val temp = Some(
            Map(
                "indicators" -> toJson(
                    Map(
                        "marketSumSales" -> toJson(
                            Map(
                                "currentNumber" -> toJson(getFormatSales(max.getMaxResultSales)),
                                "lastYearPercentage" -> toJson(max.getLastYearResultSalesPercentage)
                            )
                        ),
                        "productSales" -> toJson(
                            Map(
                                "currentNumber" -> toJson(getFormatSales(max.getCurrCompanySales)),
                                "lastYearPercentage" -> toJson(max.getLastYearCurrCompanySalesPercentage)
                            )
                        )
                    )
                ),
                "trend" -> toJson(max.getLastSeveralMonthResultSalesLst(12)),
                "region" -> toJson(
                    max.getProvLstMap.map(item =>
                        Map(
                            "name" -> item("Province"),
                            "value" -> item("TotalSales"),
                            "productSales" -> item("CompanySales"),
                            "percentage" -> item("Share")
                        )
                    )
                ),
                "mirror" -> toJson(
                    Map(
                        "provinces" -> toJson(
                            Map(
                                "current" -> toJson(
                                    max.getProvLstMap.take(10).map(item => Map(
                                        "area" -> item("Province"),
                                        "marketSales" -> item("TotalSales"),
                                        "productSales" -> item("CompanySales"),
                                        "percentage" -> item("Share")
                                    ))
                                ),
                                "lastyear" -> toJson(
                                    max.getProvLstMap.take(10).map(item => Map(
                                        "area" -> item("Province"),
                                        "marketSales" -> item("lastYearYMTotalSales"),
                                        "productSales" -> item("CompanySales"),
                                        "percentage" -> item("lastYearYMShare")
                                    ))
                                )
                            )
                        ),
                        "city" -> toJson(
                            Map(
                                "current" -> toJson(
                                    max.getCityLstMap.take(10).map(item => Map(
                                        "area" -> item("City"),
                                        "marketSales" -> item("TotalSales"),
                                        "productSales" -> item("CompanySales"),
                                        "percentage" -> item("Share")
                                    ))
                                ),
                                "lastyear" -> toJson(
                                    max.getCityLstMap.take(10).map(item => Map(
                                        "area" -> item("City"),
                                        "marketSales" -> item("lastYearYMTotalSales"),
                                        "productSales" -> item("CompanySales"),
                                        "percentage" -> item("lastYearYMShare")
                                    ))
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
