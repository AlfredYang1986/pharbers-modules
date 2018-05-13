package com.pharbers.calc.actions

import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity

object phMaxInfo2RedisAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxInfo2RedisAction(args)
}

class phMaxInfo2RedisAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "phMaxInfo2RedisAction"
    override def perform(pr: pActionArgs): pActionArgs = {
        val rd = new PhRedisDriver()

        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val panelName = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val maxDF = pr.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get

        val userJobsKey = Sercurity.md5Hash(user + company)
        val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)

        rd.addSet(userJobsKey, singleJobKey)
        rd.addMap(singleJobKey, "max_result_name", panelName)//TODO 和panel的一个名字
        val max_count = maxDF.count()
        rd.addMap(singleJobKey, "max_count", max_count)

        StringArgs(singleJobKey)
    }
}