package com.pharbers.panel.astellas

import com.pharbers.delivery.astellas.phAstellasDeliveryActions
import com.pharbers.delivery.util.phDeliveryTrait
import com.pharbers.pactions.actionbase.{MapArgs, RDDArgs}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.collection.immutable.Map
import com.pharbers.panel.phPanelTrait

/**
  * Created by clock on 18-3-7.
  */
case class phAstellasHandle(args: Map[String, List[String]]) extends phPanelTrait with phDeliveryTrait {

    override def calcYM: JsValue = {
        val mapArgs = phAstellasCalcYMActions(args).perform()
        val lst = mapArgs.asInstanceOf[MapArgs]
                .get("ymLst").asInstanceOf[RDDArgs[(String, Int)]]
                .get.collect()
        val maxYm = lst.map(_._2).max
        val result = lst.filter(_._2 > maxYm/2).map(_._1).sorted
        toJson(result.mkString(comma))
    }

    override def getPanelFile(ym: List[String], mkt: String, t: Int, c: Int): JsValue = {
        phAstellasPanelActions(args)(ym, mkt).perform()
        toJson("")
    }

    override def generateDeliveryFile: Unit = {
        phAstellasDeliveryActions(args).perform()
    }
}