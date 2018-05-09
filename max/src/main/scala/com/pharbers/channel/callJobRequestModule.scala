package com.pharbers.channel

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.channel.callJobRequestMessage._
import com.pharbers.channel.chanelImpl.responsePusher
import com.pharbers.channel.doJobActor._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

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

trait callJobRequestTrait {

    //TODO 不会写了，先和`executeJob`写到一起
    def choiceJob(jv: JsValue)
                 (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = (Some(Map().empty), None)

    def executeJob(jv: JsValue)
                  (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val as: ActorSystem = cm.modules.get.get("as").map(x => x.asInstanceOf[ActorSystem]).getOrElse(throw new Exception("actor is not impl"))
        // TODO 写死的testGroup去掉
        val jobActor = as.actorOf(doJobActor.props)

        (jv \ "call").asOpt[String].get match {
            case "ymCalc" => jobActor ! msg_doYmCalc(jv)
            case "panel" => jobActor ! msg_doPanel(jv)
            case "calc" => jobActor ! msg_doCalc(jv)
            case "kill" => jobActor ! msg_doKill(jv)
            case _ => ???
        }

        (Some(Map().empty), None)
    }

    def responseJob(jv: JsValue)
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
        responsePusher().callJobResponse(toJson("call job -> success"), "start")(jv)
        (Some(Map().empty), None)
    }

}