package com.pharbers.panel.pfizer

import com.pharbers.panel.util.phDataHandle
import play.api.libs.json.JsValue

import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */
trait phPfizerHandleTrait extends phDataHandle{
    def calcYM: JsValue
    def getPanelFile(ym: List[String]): JsValue
}
