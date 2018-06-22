package com.pharbers.builder.maintenance

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.builder.phMarketTable.phMarketManager

/**
  * Created by jeorch on 18-6-6.
  */
trait MaintenanceSearchTrait extends phMarketManager {
    def getAllCompaniesLst: (Option[Map[String, JsValue]], Option[JsValue]) = {
        val result = getAllCompanies.map(x => Map("company_id" -> x("company"), "company_name" -> x("company_name")))
        (Some(Map("companies" -> toJson(result))), None)
    }

    def getSingleModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company = (jv \ "condition" \ "maintenance" \ "company_id").asOpt[String].get
        val module_tag = (jv \ "condition" \ "maintenance" \ "module_tag").asOpt[String].get
        getModuleArgs(company)(getModuleTitleByTag(module_tag))(getModuleMatchFilesByTag(module_tag))
    }

}
