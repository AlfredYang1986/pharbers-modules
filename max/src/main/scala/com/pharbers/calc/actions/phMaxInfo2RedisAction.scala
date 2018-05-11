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

        val max_result = pr.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val panelName = defaultArgs.asInstanceOf[StringArgs].get

        val redisDriver = new PhRedisDriver()
        //TODO : uid暂时写死,供测试
        val uid = "uid"
        val company = redisDriver.getMapValue(uid, "company")
        val ym = redisDriver.getMapValue(panelName,"ym")
        val mkt = redisDriver.getMapValue(panelName,"mkt")
        val userJobsKey = Sercurity.md5Hash(s"$uid$company")
        val singleJobKey = Sercurity.md5Hash(s"$uid$company$ym$mkt")
        redisDriver.addSet(userJobsKey,singleJobKey)
        redisDriver.addMap(singleJobKey, "max_result_name", panelName)
        val max_count = max_result.count()
        redisDriver.addMap(singleJobKey, "max_count", max_count)
        StringArgs(singleJobKey)
    }
}