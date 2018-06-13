package com.pharbers.builder.phMarketTable

trait phReflectCheck { this: phMarketDBTrait with phMarketManager  =>

    val ck_user_id: Map[String, String] => Boolean = m => m.contains("user_id") && m("user_id").nonEmpty
    val ck_company_id: Map[String, String] => Boolean = m => m.contains("company_id") && m("company_id").nonEmpty
    val ck_job_id: Map[String, String] => Boolean = m => m.contains("job_id") && m("job_id").nonEmpty
    val ck_base: Map[String, String] => Boolean = m => ck_user_id(m) && ck_company_id(m) && ck_job_id(m)

    val ck_ym: Map[String, String] => Boolean = m => m.contains("ym") && m("ym").nonEmpty
    val ck_mkt: Map[String, String] => Boolean = m => m.contains("mkt") && m("mkt").nonEmpty
    val ck_progress: Map[String, String] => Boolean = m => m.contains("p_total") && m("p_total").nonEmpty && m.contains("p_current") && m("p_current").nonEmpty
    val ck_panel: Map[String, String] => Boolean = m => ck_ym(m) && ck_mkt(m) && ck_progress(m)

    val ck_max: Map[String, String] => Boolean = m => m.contains("panel_name") && m("panel_name").nonEmpty

    def parametCheck(argLst: List[String], args: Map[String, String])
                    (ckFunc: Map[String, String] => Boolean): Boolean = {
        val ckResult = argLst.map(x => args.contains(x) && args(x).nonEmpty).find(_ == false) match {
            case None => true
            case _ => false
        }
        ckResult && ckFunc(args)
    }

}