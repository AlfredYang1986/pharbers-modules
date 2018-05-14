package com.pharbers.builder

import akka.actor.Actor
import com.pharbers.builder.mapping.universeMapping
import com.pharbers.calc.phMaxJob
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

case class MaxBuilder(override val company: String, override val user: String, override val job_id: String)
                     (implicit override val actor: Actor) extends phBuilder with universeMapping {

    val ck_mkt: Map[String, String] => Boolean = m => m.contains("mkt")
    val ck_panel: Map[String, String] => Boolean = m => m.contains("panel")
    val ck_universe: Map[String, String] => Boolean = m => m.contains("universe")

    val ck_jobTotal: Map[String, String] => Boolean = m => m.contains("jobTotal")
    val ck_currentJob: Map[String, String] => Boolean = m => m.contains("currentJob")

    val ck_common: Map[String, String] => Boolean = m => ck_mkt(m) && ck_panel(m) && ck_universe(m) && ck_jobTotal(m) && ck_currentJob(m)

    override def instance: pActionTrait = {
        (company, args("mkt")) match {
            case (_, _) if ck_common(args) => phMaxJob(company, user, job_id)(args("ym"), args("mkt"), args("panel"), args("universe"), args("currentJob").toInt, args("jobTotal").toInt)
            case ("pfizer", "CNS_R") if ck_common(args) => phMaxJob(company, user, job_id)(args("ym"), args("mkt"), args("panel"), args("universe"), args("currentJob").toInt, args("jobTotal").toInt)
            case _ => throw new Exception("input wrong")
        }
    }

    override def result: JsValue = {
        val rd = new PhRedisDriver()
        val panelLst = rd.getSetAllValue(job_id)
        args += "jobTotal" -> panelLst.size.toString

        panelLst.map { panel =>
            val mkt = rd.getMapValue(panel, "mkt")
            args += "mkt" -> mkt
            args += "panel" -> panel
            args += "ym" -> rd.getMapValue(panel, "ym")
            args += "universe" -> getUniverse(company, mkt)
            args += "currentJob" -> (args.getOrElse("currentJob", "0").toInt + 1).toString
            instance.perform(MapArgs(Map().empty)).asInstanceOf[MapArgs].get("max_persistent_action").asInstanceOf[StringArgs].get
        }

        toJson("panel result")
    }

}
