package com.pharbers.builder

import akka.actor.Actor
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}

case class MaxBuilder(override val user: String, override val company: String)
                     (implicit override val actor: Actor) extends phBuilder {
    override val instance: pActionTrait = ???

    override val result: JsValue = {

        toJson(instance.perform(MapArgs(Map().empty)).asInstanceOf[MapArgs].get("max_bson_action").asInstanceOf[StringArgs].get)
    }
}
