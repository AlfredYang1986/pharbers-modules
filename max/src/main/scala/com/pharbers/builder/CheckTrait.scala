package com.pharbers.builder

trait CheckTrait { this: MarketTable =>

    val ck_user_id: Map[String, String] => Boolean = m => m.contains("user_id") && m("user_id").nonEmpty
    val ck_company_id: Map[String, String] => Boolean = m => m.contains("company_id") && m("company_id").nonEmpty
    val ck_job_id: Map[String, String] => Boolean = m => m.contains("job_id") && m("job_id").nonEmpty
    val ck_base: Map[String, String] => Boolean = m => ck_user_id(m) && ck_company_id(m) && ck_job_id(m)

    val ck_ym: Map[String, String] => Boolean = m => m.contains("ym") && m("ym").nonEmpty
    val ck_mkt: Map[String, String] => Boolean = m => m.contains("mkt") && m("mkt").nonEmpty
    val ck_progress: Map[String, String] => Boolean = m => m.contains("p_total") && m("p_total").nonEmpty && m.contains("p_current") && m("p_current").nonEmpty
    val ck_panel: Map[String, String] => Boolean = m => ck_ym(m) && ck_mkt(m) && ck_progress(m)

    val ck_max: Map[String, String] => Boolean = m => m.contains("panel_name") && m("panel_name").nonEmpty

    def getAllMkt(company: String): List[String] = marketTable.filter(company == _("company")).map(_("market"))

    def getSubsidiary(company: String): Option[Array[String]] = marketTable.find(company == _("company")).head.get("subsidiary").map(_.split("#"))

    def getTable(company: String, market: String): Map[String, String] =
        marketTable.find(x => company == x("company") && market == x("market")).getOrElse(throw new Exception("input wrong"))

    def getSourceLst(company: String, market: String): Array[String] =
        getTable(company, market).getOrElse("source", throw new Exception("input wrong")).split("#")

    def getPanelArgLst(company: String, market: String): Array[String] =
        getTable(company, market).getOrElse("panelArgs", throw new Exception("input wrong")).split("#")

    def getMaxArgLst(company: String, market: String): Array[String] =
        getTable(company, market).getOrElse("maxArgs", throw new Exception("input wrong")).split("#")

    def getDeliveryArgLst(company: String, market: String): Array[String] =
        getTable(company, market).getOrElse("deliveryArgs", throw new Exception("input wrong")).split("#")

    def getPanelArgs(company: String, market: String): Map[String, String] = {
        val table = getTable(company, market)
        getPanelArgLst(company, market).map(x => x -> table(x)).toMap
    }

    def getMaxArgs(company: String, market: String): Map[String, String] = {
        val table = getTable(company, market)
        getMaxArgLst(company, market).map(x => x -> table(x)).toMap
    }

    def getDeliveryArgs(company: String, market: String): Map[String, String] = {
        val table = getTable(company, market)
        getDeliveryArgLst(company, market).map(x => x -> table(x)).toMap
    }

    def parametCheck(argLst: Array[String], args: Map[String, String])
                    (ckFunc: Map[String, String] => Boolean): Boolean = {
        val ckResult = argLst.map(x => args.contains(x) && args(x).nonEmpty).find(_ == false) match {
            case None => true
            case _ => false
        }
        ckResult && ckFunc(args)
    }
}