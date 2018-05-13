package com.pharbers.panel.common

import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity

object phPanelInfo2Redis {
    def apply(args: MapArgs): pActionTrait = new phPanelInfo2Redis(args)
}

class phPanelInfo2Redis(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "phPanelInfo2Redis"
    override def perform(pr: pActionArgs): pActionArgs = {
        val panelDF = pr.asInstanceOf[MapArgs].get("panel").asInstanceOf[DFArgs].get

        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val panel_name = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val job_id = defaultArgs.asInstanceOf[MapArgs].get("job_id").asInstanceOf[StringArgs].get

        val panel_hosp_count = panelDF.select("HOSP_ID").distinct().count()
        val panel_prod_count = panelDF.select("Prod_Name").distinct().count()
        val panel_sales = panelDF.agg(Map("Sales" -> "sum")).take(1)(0).toString().split('[').last.split(']').head.toDouble

        val rd = new PhRedisDriver()
        val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)

        rd.addSet(job_id, panel_name)

        rd.addMap(panel_name, "ym", ym)
        rd.addMap(panel_name, "mkt", mkt)

        rd.addMap(singleJobKey, "panel_name", panel_name)
        rd.addMap(singleJobKey, "panel_hosp_count", panel_hosp_count)
        rd.addMap(singleJobKey, "panel_prod_count", panel_prod_count)
        rd.addMap(singleJobKey, "panel_sales", panel_sales)

        StringArgs(singleJobKey)
    }

}