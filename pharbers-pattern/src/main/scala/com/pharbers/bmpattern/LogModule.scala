package com.pharbers.bmpattern

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.MessageDefines
import com.pharbers.bmmessages.CommonModules
import LogMessage._
import com.pharbers.ErrorCode

object LogModule extends ModuleTrait {
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case cmd : msg_log => cmd.l(cmd.ls, cmd.data)
        case _ => (None, Some(ErrorCode.errorToJson("can not parse result")))
    }
}
