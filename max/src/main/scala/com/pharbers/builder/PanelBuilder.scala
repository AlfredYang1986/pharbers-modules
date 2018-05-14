package com.pharbers.builder

import akka.actor.Actor
import play.api.libs.json.JsValue
import com.pharbers.builder.mapping._
import play.api.libs.json.Json.toJson
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.nhwa.phNhwaPanelJob

object PanelBuilder {
    def apply(company: String, user: String, job_id: String)
             (implicit actor: Actor) = new PanelBuilder(company, user, job_id)
}

class PanelBuilder(override val company: String, override val user: String, override val job_id: String)
                  (implicit override val actor: Actor) extends phBuilder
        with marketMapping with universeMapping {

    val ck_cpa: Map[String, String] => Boolean = m => m.contains("cpa")
    val ck_gycx: Map[String, String] => Boolean = m => m.contains("gycx")
    val ck_ym: Map[String, String] => Boolean = m => m.contains("ym")
    val ck_mkt: Map[String, String] => Boolean = m => m.contains("mkt")
    val ck_universe: Map[String, String] => Boolean = m => m.contains("universe")

    val ck_jobTotal: Map[String, String] => Boolean = m => m.contains("jobTotal")
    val ck_currentJob: Map[String, String] => Boolean = m => m.contains("currentJob")

    val ck_nhwa: Map[String, String] => Boolean = m => ck_cpa(m) && ck_ym(m) && ck_mkt(m) && ck_universe(m) && ck_jobTotal(m) && ck_currentJob(m)
    val ck_astellas: Map[String, String] => Boolean = m => ck_cpa(m) && ck_gycx(m) && ck_ym(m) && ck_mkt(m) && ck_universe(m) && ck_jobTotal(m) && ck_currentJob(m)

    override def instance: pActionTrait = {
        (company, args("mkt")) match {
            case ("恩华", _) if ck_nhwa(args) => phNhwaPanelJob(company, user, job_id)(args("ym"), args("mkt"), args("cpa"), args("currentJob").toInt, args("jobTotal").toInt)("nhwa/2017年未出版医院名单.xlsx", args("universe"), "nhwa/nhwa匹配表.xlsx",  "nhwa/补充医院.xlsx", "nhwa/通用名市场定义.xlsx")
            case (_, _) if ck_nhwa(args) => phNhwaPanelJob(company, user, job_id)(args("ym"), args("mkt"), args("cpa"), args("currentJob").toInt, args("jobTotal").toInt)("nhwa/2017年未出版医院名单.xlsx", args("universe"), "nhwa/nhwa匹配表.xlsx",  "nhwa/补充医院.xlsx", "nhwa/通用名市场定义.xlsx")
            case _ => throw new Exception("input wrong")
        }
    }

    override def result: JsValue = {
        val ymLst = args("yms").split("#")
        val ymSum = ymLst.length
        val mktLst = getMarketLst(company)
        val mktSum = mktLst.length
        val JobSum = ymSum * mktSum

        args += "jobTotal" -> JobSum.toString
        for (ym <- ymLst; mkt <- mktLst) yield {
            args += "ym" -> ym
            args += "mkt" -> mkt
            args += "universe" -> getUniverse(company, mkt)
            args += "currentJob" -> (args.getOrElse("currentJob", "0").toInt + 1).toString

            instance.perform(MapArgs(Map().empty)).asInstanceOf[MapArgs].get("phSavePanelJob").asInstanceOf[StringArgs].get
        }

        toJson("panel result")
    }

}
