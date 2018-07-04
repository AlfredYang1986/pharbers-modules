package com.pharbers.channel

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.bmpattern.ModuleTrait
import com.pharbers.channel.callJobRequestMessage._
import com.pharbers.channel.chanelImpl.responsePusher
import com.pharbers.channel.doJobActor._
import com.pharbers.channel.util.getJV2Map
import play.api.libs.json.JsValue

object callJobRequestModule extends ModuleTrait with callJobRequestTrait {

    override def dispatchMsg(msg: MessageDefines)
                            (pr: Option[Map[String, JsValue]])
                            (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_executeJob(data) => executeJob(data)
        case msg_responseJob(data) => responseJob(data)

        case _ => ???
    }

}

trait callJobRequestTrait extends getJV2Map {

    def executeJob(jv: JsValue)
                  (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val as: ActorSystem = cm.modules.get.get("as").map(x => x.asInstanceOf[ActorSystem]).getOrElse(throw new Exception("actor is not impl"))
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

        responsePusher().callJobResponse(Map("job_id" -> getArgs2Map(jv)("job_id")), "start")(jv)
        (Some(Map().empty), None)
    }

}