package com.pharbers.builder.search

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.driver.PhRedisDriver

trait SearchSimpleCheckSelect extends SearchAllMktTrait  {
    def searchSimpleCheckSelect(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val job_id = (jv \ "condition" \ "job_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get
        val ymLst = new PhRedisDriver().getSetAllValue(job_id + "ym").toList.sorted
        val temp = Some(
            Map(
                "years" -> toJson(ymLst),
                "markets" -> toJson(getAllMkt(company_id))
            )
        )

        (temp, None)
    }
}
