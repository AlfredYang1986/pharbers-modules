package com.pharbers.main

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage

/**
  * Created by spark on 18-4-24.
  */
abstract class msg_CallCommand extends CommonMessage("callback", callModule)

object callMessage {
    case class msg_callback(jv: JsValue) extends msg_CallCommand
}
