package com.pharbers.builder.search

import com.pharbers.search.phMaxResultInfo
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait SearchResultCheck {

    //TODO:0515 do this
    def searchResultCheck(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val job_id = (jv \ "condition" \ "job_id").asOpt[String].get
        val market = (jv \ "condition" \ "market").asOpt[String].get
        val years = (jv \ "condition" \ "years").asOpt[String].get
        val user_id = (jv \ "user" \ "user_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get

        val max = phMaxResultInfo(user_id, company_id, years, market)

        val temp = Some(
            Map(
                "indicators" -> toJson(
                    Map(
                        "marketSumSales" -> toJson(
                            Map(
                                "currentNumber" -> toJson(max.getMaxResultSales),
                                "lastYearPercentage" -> toJson(max.getLastYearResultSalesPercentage)
                            )
                        ),
                        "productSales" -> toJson(
                            Map(
                                "currentNumber" -> toJson(max.getCurrCompanySales),
                                "lastYearPercentage" -> toJson(max.getLastYearCurrCompanySalesPercentage)
                            )
                        )
                    )
                ),
                "trend" -> toJson(max.getLastSeveralMonthResultSalesLst(12).map(item => {
                    item.map(x => toJson(x._2))
                })),
                "region" -> toJson(
                    max.getProvLstMap.map(item => toJson{
                        Map(
                            "name" -> toJson(item("Province")),
                            "value" -> toJson(item("TotalSales")),
                            "prodcutSales" -> toJson(item("CompanySales")),
                            "percentage" -> toJson(item("Share"))
                        )
                    })
                ),
                "mirror" -> toJson(
                    Map(
                        "provinces" -> toJson(
                            Map(
                                "current" -> toJson(
                                    max.getProvLstMap.take(10).map(item => toJson{
                                        Map(
                                            "area" -> toJson(item("Province")),
                                            "marketSales" -> toJson(item("TotalSales")),
                                            "percentage" -> toJson(item("Share"))
                                        )
                                    })
                                ),
                                "lastyear" -> toJson(
                                    max.getProvLstMap.take(10).map(item => toJson{
                                        Map(
                                            "area" -> toJson(item("Province")),
                                            "marketSales" -> toJson(item("lastYearYMTotalSales")),
                                            "percentage" -> toJson(item("lastYearYMShare"))
                                        )
                                    })
                                )
                            )
                        ),
                        "city" -> toJson(
                            Map(
                                "current" -> toJson(
                                    max.getCityLstMap.take(10).map(item => toJson{
                                        Map(
                                            "area" -> toJson(item("City")),
                                            "marketSales" -> toJson(item("TotalSales")),
                                            "percentage" -> toJson(item("Share"))
                                        )
                                    })
                                ),
                                "lastyear" -> toJson(
                                    max.getCityLstMap.take(10).map(item => toJson{
                                        Map(
                                            "area" -> toJson(item("City")),
                                            "marketSales" -> toJson(item("lastYearYMTotalSales")),
                                            "percentage" -> toJson(item("lastYearYMShare"))
                                        )
                                    })
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
