package com.pharbers.panel2.astellas

import com.pharbers.panel2.common.phPanelInstance
import play.api.libs.json.JsValue

/**
  * Created by spark on 18-4-3.
  */
case class phAstellasHandle() extends phPanelInstance {
    override def calcYM: JsValue = ???

    override def getPanelFile(ym: List[String], mkt: String): JsValue = ???
}
