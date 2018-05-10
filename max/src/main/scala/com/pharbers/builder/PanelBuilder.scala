package com.pharbers.builder

import akka.actor.Actor
import play.api.libs.json.JsValue
import com.pharbers.builder.panel._
import play.api.libs.json.Json.toJson
import com.pharbers.pactions.actionbase._

object PanelBuilder {
    def apply(company: String, user: String)(implicit actor: Actor) = new PanelBuilder(company, user)
}

class PanelBuilder(override val company: String, override val user: String)
                       (implicit override val actor: Actor) extends phBuilder {

    val ck_cpa: Map[String, String] => Boolean = m => m.contains("cpa")
    val ck_gycx: Map[String, String] => Boolean = m => m.contains("gycx")
    val ck_ym: Map[String, String] => Boolean = m => m.contains("ym")
    val ck_mkt: Map[String, String] => Boolean = m => m.contains("mkt")

    val ck_jobTotal: Map[String, String] => Boolean = m => m.contains("jobTotal")
    val ck_currentJob: Map[String, String] => Boolean = m => m.contains("currentJob")

    val ck_nhwa: Map[String, String] => Boolean = m => ck_cpa(m) && ck_ym(m) && ck_mkt(m) && ck_jobTotal(m) && ck_currentJob(m)
    val ck_astellas: Map[String, String] => Boolean = m => ck_cpa(m) && ck_gycx(m) && ck_ym(m) && ck_mkt(m) && ck_jobTotal(m) && ck_currentJob(m)

    override def instance: pActionTrait = {
        company match {
            case "testGroup" if ck_nhwa(args) => NhwaPanelBuilder(company, user).set(args).instance
            case "Astellas" if ck_astellas(args) => AstellasPanelBuilder(company, user).set(args).instance
            case _ => throw new Exception("input wrong")
        }
    }

    override def result: JsValue = {
        val ymLst = args("yms").split("#")
        val ymSum = ymLst.length
        val mktLst = getMarketLst(company)
        val mktSum = mktLst.length
        val JobSum = ymSum * mktSum

        for (ym <- ymLst; mkt <- mktLst) yield {
            args += "ym" -> ym
            args += "mkt" -> mkt
            args += "jobTotal" -> JobSum.toString
            args += "currentJob" -> (args.getOrElse("currentJob", "0").toInt + 1).toString

            instance.perform(MapArgs(Map().empty)).asInstanceOf[MapArgs].get("phSavePanelJob").asInstanceOf[StringArgs].get
        }

        toJson("panel result")
    }

    def getMarketLst(company: String): List[String] = company match {
        case "testGroup" => "麻醉市场" :: Nil
        case "Astellas" => "麻醉市场1" :: "麻醉市场2" :: Nil
        case _ => ???
    }

}
