package com.pharbers.pfizer

import com.pharbers.phDataHandle
import play.api.libs.json.JsValue

import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */
trait phPfizerHandleTrait extends phDataHandle{
    def generatePanelFile(args: Map[String, List[String]]): JsValue
}
