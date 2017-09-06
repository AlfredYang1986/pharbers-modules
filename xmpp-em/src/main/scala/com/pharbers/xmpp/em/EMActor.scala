package com.pharbers.xmpp.em

import akka.actor.{Actor, ActorLogging}
import play.api.libs.json.JsValue

case class  DDNInit()
case class 	DDNNotifyUsers(val parameters : List[(String, JsValue)])
case class 	DDNCreateChatRoom(val parameters : (String, JsValue)*)
case class 	DDNDismissChatRoom(val parameters : (String, JsValue)*)
case class 	DDNCreateChatGroup(val parameters : (String, JsValue)*)
case class 	DDNDismissChatGroup(val parameters : (String, JsValue)*)

case class 	DDNRegisterUser(val parameters : (String, JsValue)*)

class EMActor extends Actor with ActorLogging {
    def parameters2Map(parameters : List[(String, JsValue)]) : Map[String, JsValue] = {
        var pm : Map[String, JsValue] = Map.empty
        for ((key, value) <- parameters) pm += key -> value
        pm
    }

    def receive = {
        case DDNInit() => EMNotification.getAuthTokenForEM //DDNNotification.getAuthTokenForXMPP
        case notify : DDNNotifyUsers => {
            EMNotification.nofity(parameters2Map(notify.parameters.toList))
        }
        case create : DDNCreateChatRoom => {
            sender ! EMNotification.createChatRoom(parameters2Map(create.parameters.toList))
        }
        case dismiss : DDNDismissChatRoom => {
            sender ! EMNotification.dismissChatGroup(parameters2Map(dismiss.parameters.toList))
        }
        case cg : DDNCreateChatGroup => {
            sender ! EMNotification.createChatGroup(parameters2Map(cg.parameters.toList))
        }
        case dg : DDNDismissChatGroup => {
            sender ! EMNotification.dismissChatGroup(parameters2Map(dg.parameters.toList))
        }
        case rg : DDNRegisterUser => {
            sender ! EMNotification.registerUser(parameters2Map(rg.parameters.toList))
        }
        case _ =>
    }
}
