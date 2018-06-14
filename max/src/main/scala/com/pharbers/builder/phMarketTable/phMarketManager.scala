package com.pharbers.builder.phMarketTable

import java.util.Date
import java.text.SimpleDateFormat
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import play.api.libs.json.{JsObject, JsString, JsValue}

trait phMarketManager extends phMarketDBTrait {

    def getAllCompanies: List[Map[String, String]] =
        db.queryMultipleObject(DBObject(), "company_table")(dbOutput).map{x =>
            x.map(y => y._1 -> y._2.asInstanceOf[JsString].value)
        }

    def getAllMarkets(company: String): List[String] =
        queryMultipMarketTable(DBObject("company" -> company)).map { x =>
            x("market").asInstanceOf[JsString].value
        }.distinct

    def getAllSubsidiary(company: String): List[String] =
        db.queryObject(DBObject("company" -> company), "company_table")(dbOutput).map{x =>
            x("subsidiary").asInstanceOf[JsString].value.split("#")
        }.get.toList

    def getModuleTitleByTag(tag: String): Map[String, JsValue] => Map[String, String] = mjv => {
        val cleanObj = mjv(tag).as[JsObject].value.toMap
        Map("module_title" -> cleanObj("des").as[JsString].value)
    }

    def getModuleMatchFilesByTag(tag: String): Map[String, JsValue] => List[Map[String, String]] = mjv => {
        val dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val cleanObj = mjv(tag).as[JsObject].value.toMap
        cleanObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            val date = new Date(tmp("update_date").as[JsString].value.toLong)
            Map(
                "file_key" -> tmp("name").as[JsString].value,
                "file_des" -> tmp("des").as[JsString].value,
                "update_date" -> dateformat.format(date)
            )
        }
    }

    def getModuleArgs(jv: JsValue)
                     (moduleTitleFunc: Map[String, JsValue] => Map[String, String])
                     (matchFileLstFunc: Map[String, JsValue] => List[Map[String, String]]): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company = (jv \ "condition" \ "maintenance" \ "company_id").asOpt[String].get
        val companyTableLst = queryMultipMarketTable(DBObject("company" -> company))
        (Some(Map(
            "module_title" -> toJson(companyTableLst.flatMap(moduleTitleFunc).toMap),
            "match_files" -> toJson(companyTableLst.map(matchFileLstFunc))
        )), None)
    }


}