package com.pharbers.bmpattern

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.MessageDefines
import com.pharbers.bmmessages.CommonModules

trait ModuleTrait {
	def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue])
}