package com.pharbers.unitTest.action

import akka.actor.Actor
import com.pharbers.builder.Builderimpl
import com.pharbers.pactions.actionbase._

case class executeMaxAction(override val defaultArgs : pActionArgs)
                      (implicit _actor: Actor) extends pActionTrait {
    override val name: String = "max_result"

    val company: String = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
    val mkt: String = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
    val user: String = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
    val job_id: String = defaultArgs.asInstanceOf[MapArgs].get("job_id").asInstanceOf[StringArgs].get

    val cpa: String = defaultArgs.asInstanceOf[MapArgs].get("cpa").asInstanceOf[StringArgs].get
    val gycx: String = defaultArgs.asInstanceOf[MapArgs].get("gycx").asInstanceOf[StringArgs].get
    val ym: String = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get

    val builderimpl = Builderimpl()
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
        val panel = doPanel(mapping ++ getPanelArgs(company, mkt))
        
        // 执行Max
        val maxResult = doMax(mapping ++ getMaxArgs(company, mkt) ++ Map("panel_name" -> panel))
        StringArgs(maxResult)
    }

    def doPanel(mapping: Map[String, String]): String = {
        val ckArgLst = getPanelArgLst(company, mkt) ++ getSourceLst(company, mkt)

        if(!parametCheck(ckArgLst, mapping)(m => ck_base(m) && ck_panel(m)))
            throw new Exception("input wrong")

        val clazz: String = getClazz(mapping("company_id"), mkt)(panelInst)
        val result = impl(clazz, mapping).perform(MapArgs(Map().empty))
                .asInstanceOf[MapArgs]
                .get("phSavePanelJob")
                .asInstanceOf[StringArgs].get
//        phSparkDriver().sc.stop()
        result
    }

    def doMax(mapping: Map[String, String]): String = {
        val ckArgLst = getMaxArgLst(company, mkt)

        if(!parametCheck(ckArgLst, mapping)(m => ck_base(m) && ck_panel(m) && ck_max(m)))
            throw new Exception("input wrong")

        val clazz: String = getClazz(mapping("company_id"), mkt)(maxInst)
        val result = impl(clazz, mapping).perform(MapArgs(Map().empty))
                .asInstanceOf[MapArgs]
                .get("max_persistent_action")
                .asInstanceOf[StringArgs].get
//        phSparkDriver().sc.stop()
        result
    }
}
