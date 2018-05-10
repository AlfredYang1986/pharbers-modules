package com.pharbers.panel.common

import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity

object phPanelId2Redis {
    def apply(args: MapArgs): pActionTrait = new phPanelId2Redis(args)
}

class phPanelId2Redis(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "phPanelId2Redis"
    override def perform(pr: pActionArgs): pActionArgs = {
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val panel_name = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get

        val rd = new PhRedisDriver()
        val singleJobKey = Sercurity.md5Hash(s"$company$ym$mkt")

        rd.addMap(panel_name, "ym", ym)
        rd.addMap(panel_name, "mkt", mkt)
        rd.addMap(singleJobKey, "panel_path", panel_name)

        StringArgs(panel_name)
    }

}