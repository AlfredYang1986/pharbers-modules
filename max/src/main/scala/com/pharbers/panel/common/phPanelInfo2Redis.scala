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
        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val panel_name = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val panelDF = pr.asInstanceOf[MapArgs].get("panel").asInstanceOf[DFArgs].get

        val panel_hosp_count = panelDF.select("HOSP_ID").distinct().count()
        val panel_prod_count = panelDF.select("Prod_Name").distinct().count()
        val panel_sales = panelDF.agg(Map("Sales" -> "sum")).take(1)(0).toString().split('[').last.split(']').head.toDouble

        val redisDriver = new PhRedisDriver()
        //TODO : uid暂时写死,供测试
        val uid = "uid"
        val company = redisDriver.getMapValue(uid, "company")
        val singleJobKey = Sercurity.md5Hash(s"$uid$company$ym$mkt")
        redisDriver.addMap(panel_name, "ym", ym)
        redisDriver.addMap(panel_name, "mkt", mkt)
        redisDriver.addMap(singleJobKey, "panel_name", panel_name)

        StringArgs(panel_name)
    }

}