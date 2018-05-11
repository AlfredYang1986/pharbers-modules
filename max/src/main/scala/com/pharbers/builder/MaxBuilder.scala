package com.pharbers.builder

import akka.actor.Actor
import com.pharbers.calc.phMaxJob
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}

case class MaxBuilder(override val user: String, override val company: String)
                     (implicit override val actor: Actor) extends phBuilder {
    val ck_jobID: Map[String, String] => Boolean = m => m.contains("jobId")

    val ck_jobTotal: Map[String, String] => Boolean = m => m.contains("jobTotal")
    val ck_currentJob: Map[String, String] => Boolean = m => m.contains("currentJob")

    override def instance: pActionTrait = {
        phMaxJob("1fab79bf-935e-4253-9fa8-567230e5f94c", "nhwa/universe_麻醉市场_online.xlsx")
    }

    override def result: JsValue = {
        toJson(instance.perform(MapArgs(Map().empty)).asInstanceOf[MapArgs].get("max_bson_action").asInstanceOf[StringArgs].get)
    }
}
