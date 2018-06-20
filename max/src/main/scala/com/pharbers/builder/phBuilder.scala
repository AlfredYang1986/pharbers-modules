package com.pharbers.builder

import akka.actor.Actor
import com.pharbers.builder.phMarketTable.Builderimpl
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase.{JVArgs, MapArgs, StringArgs}
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

    val builderimpl = Builderimpl(mapping("company_id"))
    import builderimpl._

    def doCalcYM(): JsValue = {
        val ymInstMap = getYmInst
        val ckArgLst = ymInstMap("source").split("#").toList

        if(!parametCheck(ckArgLst, mapping)(ck_base))
            throw new Exception("input wrong")

        val clazz: String = ymInstMap("instance")
        val result = impl(clazz, mapping).perform(MapArgs(Map().empty))
                .asInstanceOf[MapArgs].get("result").asInstanceOf[JVArgs].get
        phSparkDriver().sc.stop()

        result
    }

    def doPanel(): JsValue = {
        val ymLst = mapping("yms").split("#")
        val jobSum = ymLst.length * mktLst.length
        mapping += "p_total" -> jobSum.toString

        for (ym <- ymLst; mkt <- mktLst) {
            mapping += "ym" -> ym
            mapping += "mkt" -> mkt
            val panelInstMap = getPanelInst(mkt)
            val ckArgLst = panelInstMap("source").split("#").toList ::: panelInstMap("args").split("#").toList ::: Nil
            mapping ++= panelInstMap
            mapping += "p_current" -> (mapping.getOrElse("p_current", "0").toInt + 1).toString

            if(!parametCheck(ckArgLst, mapping)(m => ck_base(m) && ck_panel(m)))
                throw new Exception("input wrong")

            val clazz: String = panelInstMap("instance")
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

        val maxResult = panelLst.map { panel =>
            val mkt = rd.getMapValue(panel, "mkt")
            val ym = rd.getMapValue(panel, "ym")
            val maxInstMap = getMaxInst(mkt)
            mapping += "ym" -> ym
            mapping += "mkt" -> mkt
            mapping += "panel_name" -> panel
            mapping += "p_current" -> (mapping.getOrElse("p_current", "0").toInt + 1).toString
            mapping ++= maxInstMap

            val ckArgLst = maxInstMap("args").split("#").toList ::: Nil

            if(!parametCheck(ckArgLst, mapping)(m => ck_base(m) && ck_panel(m) && ck_max(m)))
                throw new Exception("input wrong")

            val clazz: String = maxInstMap("instance")
            val result = impl(clazz, mapping).perform(MapArgs(Map().empty))
                    .asInstanceOf[MapArgs]
                    .get("max_persistent_action")
                    .asInstanceOf[StringArgs].get
            phSparkDriver().sc.stop()
            result
        }

        toJson(maxResult)
    }

}
