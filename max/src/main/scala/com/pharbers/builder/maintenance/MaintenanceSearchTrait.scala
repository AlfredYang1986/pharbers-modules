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

    def getDataCleanModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) =
        getModuleArgs(jv)(getModuleTitleByTag("clean"))(getModuleMatchFilesByTag("clean"))

    def getSimpleModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) =
        getModuleArgs(jv)(getModuleTitleByTag("panel"))(getModuleMatchFilesByTag("panel"))

    def getMaxModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) =
        getModuleArgs(jv)(getModuleTitleByTag("max"))(getModuleMatchFilesByTag("max"))

    def getDeliveryModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) =
        getModuleArgs(jv)(getModuleTitleByTag("delivery"))(getModuleMatchFilesByTag("delivery"))

}
