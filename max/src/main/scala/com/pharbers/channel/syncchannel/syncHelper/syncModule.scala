package com.pharbers.channel.syncchannel.syncHelper

import akka.actor.{Actor, ActorRef}
import com.pharbers.ErrorCode
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.channel.syncchannel.syncHelper.syncMessage.msg_syncCallback
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

case class syncModule(t : ActorRef) extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])(implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_syncCallback(data) => pSyncCallback(data)
        case _ => ???
    }

    def pSyncCallback(data : JsValue)
                     (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            t ! data
            (Some(Map("status" -> toJson("ok"))), None)

        } catch {
            case ex : Exception => println(s"sync call back.error=${ex.getMessage}");(None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }
}
