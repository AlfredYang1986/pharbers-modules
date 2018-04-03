package com.pharbers.panel2.common

import play.api.libs.json.JsValue

/**
  * Created by clock on 18-3-6.
  */
trait phPanelTrait extends phDataHandleTrait {
    def calcYM: JsValue
    def getPanelFile(ym: List[String], mkt: String): JsValue
}