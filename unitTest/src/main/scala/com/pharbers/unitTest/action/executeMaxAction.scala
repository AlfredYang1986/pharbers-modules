package com.pharbers.unitTest.action

import akka.actor.Actor
import com.pharbers.pactions.actionbase._
import com.pharbers.unitTest.common.readJsonTrait
import com.pharbers.builder.phMarketTable.Builderimpl

case class executeMaxAction(override val defaultArgs : pActionArgs)
                      (implicit _actor: Actor) extends pActionTrait with readJsonTrait {
    override val name: String = "max_result"

    val company: String = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
    val mkt: String = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
    val user: String = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
    val job_id: String = defaultArgs.asInstanceOf[MapArgs].get("job_id").asInstanceOf[StringArgs].get

    val cpa: String = defaultArgs.asInstanceOf[MapArgs].get("cpa").asInstanceOf[StringArgs].get
    val gycx: String = defaultArgs.asInstanceOf[MapArgs].get("gycx").asInstanceOf[StringArgs].get
    val ym: String = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get

    val builderimpl = Builderimpl(company)
    import builderimpl._

    override def perform(args : pActionArgs): pActionArgs = {

        val mapping: Map[String, String] = Map(
            "user_id" -> user,
            "company_id" -> company,
            "job_id" -> job_id,
            "ym" -> ym,
            "mkt" -> mkt,
            "p_total" -> "0",
            "p_current" -> "0",
            "cpa" -> cpa,
            "gycx" -> gycx
        )

        // 执行Panel
        val panel = doPanel(mapping)
        
        // 执行Max
        val maxResult = doMax(mapping, panel)
        StringArgs(maxResult)
    }

    def doPanel(mapping: Map[String, String]): String = {
        val panelInstMap = getPanelInst(mkt)
        val ckArgLst = panelInstMap("source").split("#").toList ::: panelInstMap("args").split("#").toList ::: Nil
        val args = mapping ++ panelInstMap ++ testData.find(x => company == x("company") && mkt == x("market")).get

        if(!parametCheck(ckArgLst, args)(m => ck_base(m) && ck_panel(m)))
            throw new Exception("input wrong")

        val clazz: String = panelInstMap("instance")
        val result = impl(clazz, args).perform(MapArgs(Map().empty))
                .asInstanceOf[MapArgs]
                .get("phSavePanelJob")
                .asInstanceOf[StringArgs].get
//        phSparkDriver().sc.stop()
        result
    }

    def doMax(mapping: Map[String, String], panel: String): String = {
        val maxInstMap = getMaxInst(mkt)
        val ckArgLst = maxInstMap("args").split("#").toList ::: Nil
        val args = mapping ++ maxInstMap ++ Map("panel_name" -> panel) ++ testData.find(x => company == x("company") && mkt == x("market")).get

        if(!parametCheck(ckArgLst, args)(m => ck_base(m) && ck_panel(m) && ck_max(m)))
            throw new Exception("input wrong")

        val clazz: String = maxInstMap("instance")
        val result = impl(clazz, args).perform(MapArgs(Map().empty))
                .asInstanceOf[MapArgs]
                .get("max_persistent_action")
                .asInstanceOf[StringArgs].get
//        phSparkDriver().sc.stop()
        result
    }
}
