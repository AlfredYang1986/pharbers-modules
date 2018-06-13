package com.pharbers.builder.search

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.builder.CheckTrait
import com.pharbers.builder.phMarketTable.phMarketTrait

trait SearchAllMktTrait extends CheckTrait with phMarketTrait {
    def searchAllMkt(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get

        (Some(Map("markets" -> toJson(getAllMkt(company_id)))), None)
    }
}
