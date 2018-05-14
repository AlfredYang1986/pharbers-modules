package com.pharbers.builder

import akka.actor.Actor
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.pactions.jobs.NULLJob
import com.pharbers.pactions.actionbase.pActionTrait

object phBuilder {
    def apply(_company: String, _user: String, _job_id: String)
             (implicit _actor: Actor): phBuilder = {
        new phBuilder {
            override val user: String = _user
            override val job_id: String = _job_id
            override val company: String = _company
            override implicit val actor: Actor = _actor

            override def result: JsValue = toJson("")
            override def instance: pActionTrait = NULLJob
        }
    }
}

trait phBuilder {
    val user: String
    val job_id: String
    val company: String
    implicit val actor: Actor

    def result: JsValue
    def instance: pActionTrait
    var args: Map[String, String] = Map().empty

    def set(key: String, value: String): phBuilder = {
        args += key -> value
        this
    }

    def set(map: Map[String, String]): phBuilder = {
        args ++= map
        this
    }

    def doCalcYM: JsValue = YmCalcBuilder(company, user, job_id).set(args).result

    def doPanel: JsValue = PanelBuilder(company, user, job_id).set(args).result

    def doMax: JsValue = MaxBuilder(company, user, job_id).set(args).result
}
