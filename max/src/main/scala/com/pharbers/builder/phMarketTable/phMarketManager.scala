package com.pharbers.builder.phMarketTable

import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsObject, JsString, JsValue}

trait phMarketManager { this: phMarketDBTrait =>

    def getAllCompanies: List[Map[String, String]] =
        db.queryMultipleObject(DBObject(), "company_table")(dbOutput).map{x =>
            x.map(y => y._1 -> y._2.asInstanceOf[JsString].value)
        }

    def getAllMarkets(company: String): List[String] =
        queryMultipMarketTable(DBObject("company" -> company)).map { x =>
            x("market").asInstanceOf[JsString].value
        }.distinct

    val onlyCleanDes: Map[String, JsValue] => Map[String, String] = mjv => {
        val cleanObj = mjv("clean").as[JsObject].value.toMap
        val cleanFileMap = cleanObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            tmp("name").as[JsString].value -> tmp("des")
        }.toMap

        (cleanObj - "files" ++ cleanFileMap).map(x => x._1 -> x._2.as[JsString].value)
    }

    val onlyPanelDes: Map[String, JsValue] => Map[String, String] = mjv => {
        val panelObj = mjv("panel").as[JsObject].value.toMap
        val panelFileMap = panelObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            tmp("name").as[JsString].value -> tmp("des")
        }.toMap

        (panelObj - "files" ++ panelFileMap).map(x => x._1 -> x._2.as[JsString].value)
    }

    val onlyMaxDes: Map[String, JsValue] => Map[String, String] = mjv => {
        val maxObj = mjv("max").as[JsObject].value.toMap
        val maxFileMap = maxObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            tmp("name").as[JsString].value -> tmp("des")
        }.toMap

        (maxObj - "files" ++ maxFileMap).map(x => x._1 -> x._2.as[JsString].value)
    }

    val onlyDeliveryDes: Map[String, JsValue] => Map[String, String] = mjv => {
        val deliveryObj = mjv("delivery").as[JsObject].value.toMap
        val deliveryFileMap = deliveryObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            tmp("name").as[JsString].value -> tmp("des")
        }.toMap

        (deliveryObj - "files" ++ deliveryFileMap).map(x => x._1 -> x._2.as[JsString].value)
    }

    def getMatchFileDes(company: String)
                       (func: Map[String, JsValue] => Map[String, String]): Map[String, String] =
        queryMultipMarketTable(DBObject("company" -> company)).flatMap(func).toMap

}