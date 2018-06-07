package com.pharbers.builder.maintenance

import com.pharbers.builder.{CheckTrait, MarketTable}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-6-6.
  */
trait MaintenanceSearchTrait extends CheckTrait with MarketTable {
    def getAllCompanies: (Option[Map[String, JsValue]], Option[JsValue]) = {

        (Some(Map("companies" -> toJson(getMktTableAllCompanies))), None)
    }

    def getDataCleanModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) =
        getModuleArgs(jv)(getDataCleanArgs)

    def getSimpleModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) =
        getModuleArgs(jv)(getPanelArgs)

    def getMaxModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) =
        getModuleArgs(jv)(getMaxArgs)

    def getDeliveryModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) =
        getModuleArgs(jv)(getDeliveryArgs)

    def getModuleArgs(jv: JsValue)(func: (String, String) => Map[String, String]): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company_id = (jv \ "condition" \ "maintenance" \ "company_id").asOpt[String].get

        val allArgs = getAllMkt(company_id).map(x => func(company_id, x) ++ Map("Market" -> x))

        val matchFileLst: List[Map[String, String]] =
            allArgs.reduce((m1, m2) => m1 ++ m2 - "universe_file" - "Market")
                .map(x => Map("file_key" -> x._1, "file_name" -> x._2.split("/").last)).toList

        val universeFileLst: List[Map[String, String]] = allArgs match {
            case lst if lst.reduce((m1, m2) => m1 ++ m2).keys.exists(x => x == "universe_file") =>
                allArgs.map(x =>Map("file_key" -> "universe_file", "file_name" -> x("universe_file").split("/").last, "Market" -> x("Market")))
            case _ => List.empty
        }

        (Some(Map("match_tables" -> toJson(matchFileLst), "universe_files" -> toJson(universeFileLst))), None)
    }

}
