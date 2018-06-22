package com.pharbers.builder.search

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.builder.phMarketTable.{phMarketManager, phMarketDBTrait, phReflectCheck}

trait SearchAllMktTrait extends phMarketManager {
    def searchAllMkt(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get

        (Some(Map("markets" -> toJson(getAllMarkets(company_id)))), None)
    }
}
