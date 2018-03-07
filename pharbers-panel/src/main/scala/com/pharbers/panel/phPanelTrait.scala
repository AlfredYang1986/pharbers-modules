package com.pharbers.panel

import play.api.libs.json.JsValue
import com.pharbers.panel.util.common.phDataHandleTrait

/**
  * Created by clock on 18-3-6.
  */
trait phPanelTrait extends phDataHandleTrait {
    def calcYM: JsValue
    def getPanelFile(ym: List[String] = Nil, mkt: String = "", t: Int = 0, c: Int = 0): JsValue
}