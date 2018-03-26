package com.pharbers.panel.nhwa

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import scala.collection.immutable.Map
import com.pharbers.panel.phPanelTrait

/**
  * Created by clock on 18-3-7.
  */
case class phNhwaHandle(args: Map[String, List[String]]) extends phPanelTrait {

    override def calcYM: JsValue = {
        phNhwaCalcYMActions(args).perform()
        toJson("")
    }

    override def getPanelFile(ym: List[String], mkt: String, t: Int, c: Int): JsValue = {
        phNhwaPanelActions(args)(ym, mkt).perform()
        toJson("")
    }
}