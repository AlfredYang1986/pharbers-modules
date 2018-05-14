package com.pharbers.builder.search

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
