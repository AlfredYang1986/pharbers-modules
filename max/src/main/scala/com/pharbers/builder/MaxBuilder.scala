package com.pharbers.builder

import akka.actor.Actor
import play.api.libs.json.JsValue
import com.pharbers.pactions.actionbase.pActionTrait


case class MaxBuilder(override val user: String, override val company: String)
                     (implicit override val actor: Actor) extends phBuilder {
    override val result: JsValue = ???
    override val instance: pActionTrait = ???
}
