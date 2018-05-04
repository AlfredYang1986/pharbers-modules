package com.pharbers.main.callJobRequest

import play.api.libs.json.JsValue
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.main.callJobRequest.callJobRequestMessage._
import com.pharbers.bmmessages.{CommonModules, MessageDefines}

object callJobRequestModule extends ModuleTrait with callJobRequestTrait {

    override def dispatchMsg(msg: MessageDefines)
                            (pr: Option[Map[String, JsValue]])
                            (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_choiceJob(data) => choiceJob(data)
        case msg_executeJob(data) => executeJob(data)
        case msg_responseJob(data) => responseJob(data)

        case _ => ???
    }

}
