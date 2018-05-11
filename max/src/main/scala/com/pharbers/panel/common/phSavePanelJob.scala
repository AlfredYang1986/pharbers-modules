package com.pharbers.panel.common

import com.pharbers.pactions.actionbase._
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity

object phSavePanelJob  {
    def apply(args: MapArgs): pActionTrait = new phSavePanelJob(args)
}

class phSavePanelJob(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "phSavePanelJob"
    override def perform(pr: pActionArgs): pActionArgs = {
        val panel = pr.asInstanceOf[MapArgs].get("panel").asInstanceOf[DFArgs].get
        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val panel_name = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val panel_location = max_path_obj.p_panelPath + panel_name

        val redisDriver = new PhRedisDriver()
        //TODO : uid暂时写死,供测试
        val uid = "uid"
        val company = redisDriver.getMapValue(uid, "company")
        val singleJobKey = Sercurity.md5Hash(s"$uid$company$ym$mkt")
        redisDriver.addMap(panel_name, "ym", ym)
        redisDriver.addMap(panel_name, "mkt", mkt)
        redisDriver.addMap(singleJobKey, "panel_path", panel_location)

        panel.write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                .save(panel_location)

        StringArgs(panel_name)
    }
}