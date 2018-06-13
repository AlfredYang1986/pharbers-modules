package com.pharbers.builder.search

import com.pharbers.pactions.actionbase.{MapArgs, StringArgs}
import com.pharbers.search.{phDeliverySearchDataJob, phExportSearchDataJob}
import com.pharbers.spark.phSparkDriver
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait SearchDataExport {
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

}
