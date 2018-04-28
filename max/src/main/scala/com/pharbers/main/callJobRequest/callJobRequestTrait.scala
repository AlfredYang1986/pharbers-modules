package com.pharbers.main.callJobRequest

import akka.actor.ActorSystem
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.channel.responsePusher
import com.pharbers.bmmessages.CommonModules

/**
  * Created by spark on 18-4-26.
  */
trait callJobRequestTrait {

    //TODO 不会写了，先和`executeJob`写到一起
    def choiceJob(jv: JsValue)
                 (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) =
        (Some(Map[String, JsValue]().empty), None)

    def executeJob(jv: JsValue)
                  (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {
        import com.pharbers.main.callJobRequest.doJobActor._

        val as = cm.modules.get.get("as").map(x => x.asInstanceOf[ActorSystem]).getOrElse(throw new Exception("actor is not impl"))
        val doJob = as.actorOf(doJobActor.props)

        (jv \ "call").asOpt[String].get match {
            case "ymCalc" => doJob ! msg_doYmCalc(jv)
            case "panel" => doJob ! msg_doPanel(jv)
            case "calc" => doJob ! msg_doCalc(jv)
            case "kill" => doJob ! msg_doKill(jv)
            case _ => ???
        }

        (Some(Map[String, JsValue]().empty), None)
    }

    def responseJob(jv: JsValue)
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val pp: responsePusher = responsePusher()
        import pp._

        val result = toJson("call job -> success")

        callJobResponse(result)(jv)
        (Some(Map[String, JsValue]().empty), None)
    }

}

