package com.pharbers.builder.phMarketTable

import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsObject, JsString, JsValue}

trait phMarketReflectTrait { this: phMarketDBTrait =>

    val onlyYmInst: Map[String, JsValue] => Map[String, String] = mjv =>
        mjv("calcYM").as[JsObject].value.toMap.map(x => x._1 -> x._2.asInstanceOf[JsString].value)

    // 现在清洗和panel混到了一起，所以现在这样写
    val onlyPanelInst: Map[String, JsValue] => Map[String, String] = mjv => {
        val cleanObj = mjv("clean").as[JsObject].value.toMap
        val cleanFileMap = cleanObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            tmp("name").as[JsString].value -> tmp("path")
        }.toMap

        val panelObj = mjv("panel").as[JsObject].value.toMap
        val panelFileMap = panelObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            tmp("name").as[JsString].value -> tmp("path")
        }.toMap

        (panelObj - "files" ++ cleanFileMap ++ panelFileMap).map(x => x._1 -> x._2.as[JsString].value)
    }

    val onlyMaxInst: Map[String, JsValue] => Map[String, String] = mjv => {
        val maxObj = mjv("max").as[JsObject].value.toMap
        val maxFileMap = maxObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            tmp("name").as[JsString].value -> tmp("path")
        }.toMap

        (maxObj - "files" ++ maxFileMap).map(x => x._1 -> x._2.as[JsString].value)
    }

    val onlyDeliveryInst: Map[String, JsValue] => Map[String, String] = mjv => {
        val deliveryObj = mjv("delivery").as[JsObject].value.toMap
        val deliveryFileMap = deliveryObj("files").as[JsObject].value.toList.map{x =>
            val tmp = x._2.as[JsObject].value.toMap
            tmp("name").as[JsString].value -> tmp("path")
        }.toMap

        (deliveryObj - "files" ++ deliveryFileMap).map(x => x._1 -> x._2.as[JsString].value)
    }

    def queryInstance(company: String, market: String)
                     (func: Map[String, JsValue] => Map[String, String]): Option[Map[String, String]] =
        queryMarketTable(DBObject("company" -> company, "market" -> market)).map(func)

}