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