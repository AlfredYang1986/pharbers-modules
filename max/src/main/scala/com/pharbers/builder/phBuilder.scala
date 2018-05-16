package com.pharbers.builder

import akka.actor.Actor
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase.{JVArgs, MapArgs, NULLArgs, StringArgs}
import com.pharbers.spark.phSparkDriver

object phBuilder {
    def apply(_company: String, _user: String, _job_id: String)
             (implicit _actor: Actor): phBuilder = {
        new phBuilder {
            override lazy val user: String = _user
            override lazy val job_id: String = _job_id
            override lazy val company: String = _company
            override implicit val actor: Actor = _actor
        }
    }
}

trait phBuilder {
    val user: String
    val job_id: String
    val company: String
    implicit val actor: Actor

    var mapping: Map[String, String] = Map(
        "user_id" -> user,
        "company_id" -> company,
        "job_id" -> job_id
    )

    def set(key: String, value: String): phBuilder = {
        mapping += key -> value
        this
    }

    def set(map: Map[String, String]): phBuilder = {
        mapping ++= map
        this
    }

    val builderimpl = Builderimpl()
    import builderimpl._

    def doCalcYM(): JsValue = {
        val defaultMkt = getAllMkt(mapping("company_id")).get.head
        val ckArgLst = getSourceLst(mapping("company_id"), defaultMkt)

        if(!parametCheck(ckArgLst, mapping)(ck_base))
            throw new Exception("input wrong")

        val clazz: String = getClazz(mapping("company_id"), defaultMkt)(ymInst)
        val result = impl(clazz, mapping).perform(NULLArgs).asInstanceOf[JVArgs].get
        phSparkDriver().sc.stop()
        result
    }

    def doPanel(): JsValue = {
        val ymLst = mapping("yms").split("#")
        val mktLst = getAllMkt(mapping("company_id")).get
        val jobSum = ymLst.length * mktLst.length
        mapping += "p_total" -> jobSum.toString

        for (ym <- ymLst; mkt <- mktLst) {
            mapping += "ym" -> ym
            mapping += "mkt" -> mkt
            val ckArgLst = getArgLst(mapping("company_id"), mkt) ++ getSourceLst(mapping("company_id"), mkt)
            mapping ++= getPanelArgs(mapping("company_id"), mkt)
            mapping += "p_current" -> (mapping.getOrElse("p_current", "0").toInt + 1).toString

            if(!parametCheck(ckArgLst, mapping)(m => ck_base(m) && ck_panel(m)))
                throw new Exception("input wrong")

            val clazz: String = getClazz(mapping("company_id"), mkt)(panelInst)
            val result = impl(clazz, mapping).perform(MapArgs(Map().empty))
                    .asInstanceOf[MapArgs]
                    .get("phSavePanelJob")
                    .asInstanceOf[StringArgs].get
            phSparkDriver().sc.stop()
            result
        }

        toJson(mapping("job_id"))
    }

    def doMax(): JsValue = {
        val rd = new PhRedisDriver()
        val panelLst = rd.getSetAllValue(mapping("job_id"))
        mapping += "p_total" -> panelLst.size.toString

        panelLst.foreach { panel =>
            val mkt = rd.getMapValue(panel, "mkt")
            mapping += "mkt" -> mkt
            mapping += "panel_name" -> panel
            mapping += "ym" -> rd.getMapValue(panel, "ym")
            mapping += "universe_file" -> getPanelArgs(mapping("company_id"), mkt)("universe_file")
            mapping += "p_current" -> (mapping.getOrElse("p_current", "0").toInt + 1).toString

            if(!parametCheck(Array("universe_file"), mapping)(m => ck_base(m) && ck_panel(m) && ck_max(m)))
                throw new Exception("input wrong")

            val clazz: String = getClazz(mapping("company_id"), mkt)(maxInst)
            val result = impl(clazz, mapping).perform(MapArgs(Map().empty))
                    .asInstanceOf[MapArgs]
                    .get("max_persistent_action")
                    .asInstanceOf[StringArgs].get
            phSparkDriver().sc.stop()
            result
        }

        toJson("max result")
    }

}
