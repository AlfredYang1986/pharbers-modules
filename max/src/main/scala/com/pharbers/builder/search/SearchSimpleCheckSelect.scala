package com.pharbers.builder.search

import com.pharbers.builder.mapping.marketMapping
import com.pharbers.driver.PhRedisDriver
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait SearchSimpleCheckSelect { this: marketMapping =>
    def searchSimpleCheckSelect(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val job_id = (jv \ "condition" \ "job_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get
        val ymLst = new PhRedisDriver().getSetAllValue(job_id + "ym")
        val temp = Some(
            Map(
                "years" -> toJson(ymLst),
                "markets" -> toJson(getMarketLst(company_id))
            )
        )

        (temp, None)
    }
}
