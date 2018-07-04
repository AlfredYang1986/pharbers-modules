package com.pharbers.builder.search

import com.mongodb.casbah.Imports.DBObject
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs}
import com.pharbers.search.{phDeliverySearchDataJob, phExportSearchDataJob}
import com.pharbers.builder.phMarketTable.{phMarketManager, phMarketReflectTrait}

trait SearchDataExport extends phMarketReflectTrait with phMarketManager {
    def exportData(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company = (jv \ "user" \ "company" \ "company_id").asOpt[String].getOrElse(throw new Exception("Illegal company"))
        val market = (jv \ "condition" \ "market").asOpt[String].getOrElse("")
        val startTime = (jv \ "condition" \ "startTime").asOpt[String].getOrElse("")
        val endTime = (jv \ "condition" \ "endTime").asOpt[String].getOrElse("")

        val args: Map[String, String] = Map(
            "company" -> company,
            "ym_condition" -> s"$startTime-$endTime",
            "mkt" -> market
        )

        val exportResult =  phExportSearchDataJob(args).perform().asInstanceOf[MapArgs].get("export_search_data_action").asInstanceOf[StringArgs].get
        phSparkDriver().sc.stop

        (Some(Map("export_file_name" -> toJson(exportResult))), None)
    }

    def exportDelivery(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company = (jv \ "user" \ "company" \ "company_id").asOpt[String].getOrElse(throw new Exception("Illegal company"))
        val market = (jv \ "condition" \ "market").asOpt[String].getOrElse("")
        val startTime = (jv \ "condition" \ "startTime").asOpt[String].getOrElse("")
        val endTime = (jv \ "condition" \ "endTime").asOpt[String].getOrElse("")

        val args: Map[String, String] = Map(
            "company" -> company,
            "ym_condition" -> s"$startTime-$endTime",
            "mkt" -> market
        )

        val exportResult =  phDeliverySearchDataJob(args).perform().asInstanceOf[MapArgs].get("export_delivery_data_action").asInstanceOf[MapArgs].get("delivery_data_action").asInstanceOf[StringArgs].get
        phSparkDriver().sc.stop

        (Some(Map("delivery_file_name" -> toJson(exportResult))), None)
    }

    def getCurrentCompanyExportType(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company = (jv \ "user" \ "company" \ "company_id").asOpt[String].getOrElse(throw new Exception("Illegal company"))
        val company_name = getAllCompanies.find(company == _("company")).getOrElse(throw new Exception("You account has no calc lincense!"))("company_name")
        val exportTypeLst: List[String] = queryMultipMarketTable(DBObject("company" -> company))
                .map(onlyDeliveryInst).map(_("instance")).distinct.filter(_ != "") match {
            case Nil => "Max格式" :: Nil
            case _ => "Max格式" :: s"${company_name}交付格式" :: Nil
        }

        (Some(Map("export_data_type" -> toJson(exportTypeLst))), None)
    }

}
