package com.pharbers.channel.chanelImpl

import com.pharbers.bmmessages.CommonMessage
import com.pharbers.channel.callJobRequestModule
import play.api.libs.json.JsValue

/**
  * Created by spark on 18-4-24.
  */
class msg_callJobRequest extends CommonMessage("callJobRequest", callJobRequestModule)

object callJobRequestMessage {
    case class msg_choiceJob(jv: JsValue) extends msg_callJobRequest
    case class msg_executeJob(jv: JsValue) extends msg_callJobRequest
    case class msg_responseJob(jv: JsValue) extends msg_callJobRequest
}
