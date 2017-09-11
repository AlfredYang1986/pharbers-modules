package com.pharbers.bmpattern

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.MessageDefines
import com.pharbers.bmmessages.CommonModules
import ResultMessage.msg_CommonResultMessage
import com.pharbers.ErrorCode

object ResultModule extends ModuleTrait {
	def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
		case cmd : msg_CommonResultMessage => cmd.func(pr.get)
		case _ => (None, Some(ErrorCode.errorToJson("can not parse result")))
	}
}