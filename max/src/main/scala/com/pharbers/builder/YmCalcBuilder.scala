package com.pharbers.builder

import akka.actor.Actor
import com.pharbers.pactions.actionbase.{JVArgs, NULLArgs, pActionTrait}
import com.pharbers.panel.nhwa.phNhwaCalcYMJob
import play.api.libs.json.JsValue

case class YmCalcBuilder(override val company: String, override val user: String, override val job_id: String)
                        (implicit override val actor: Actor) extends phBuilder {

    val ck_cpa: Map[String, String] => Boolean = m => m.contains("cpa")
    val ck_gycx: Map[String, String] => Boolean = m => m.contains("gycx")
    val ck_cpagycx: Map[String, String] => Boolean = m => ck_cpa(m) && ck_gycx(m)

    override def instance: pActionTrait = {
        company match {
            case "恩华" if ck_cpa(args) => phNhwaCalcYMJob(company, user)(args("cpa"))
            case _ => throw new Exception("input wrong")
        }
    }

    override def result: JsValue = instance.perform(NULLArgs).asInstanceOf[JVArgs].get

}
