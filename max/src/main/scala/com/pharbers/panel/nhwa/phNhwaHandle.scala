package com.pharbers.panel.nhwa

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import scala.collection.immutable.Map
import com.pharbers.panel.phPanelTrait
import com.pharbers.pactions.actionbase.RDDArgs
import com.pharbers.delivery.util.phDeliveryTrait
import com.pharbers.delivery.nhwa.phNhwaDeliveryActions

/**
  * Created by clock on 18-3-7.
  */

case class phNhwaHandle(args: Map[String, List[String]]) extends phPanelTrait with phDeliveryTrait{

    override def calcYM: JsValue = {
//        val lst = phNhwaCalcYMJob(args).perform().asInstanceOf[RDDArgs[(String, Int)]].get.collect()
//        val maxYm = lst.map(_._2).max
//        val result = lst.filter(_._2 > maxYm/2).map(_._1).sorted
//        toJson(result.mkString(comma))
        toJson("")
    }


    override def getPanelFile(ym: List[String], mkt: String, t: Int, c: Int): JsValue = {
//        phNhwaPanelJob(args)(ym, mkt).perform()
        toJson("")
    }

    override def generateDeliveryFile: Unit = {
        phNhwaDeliveryActions(args).perform()
    }
}