package com.pharbers.search.actions

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver

/**
  * Created by jeorch on 18-5-11.
  */
object phReadMaxResultAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phReadMaxResultAction(args)
}

class phReadMaxResultAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "read_result_action"

    override def perform(pr: pActionArgs): pActionArgs = {
        val uid = defaultArgs.asInstanceOf[MapArgs].get("uid").asInstanceOf[StringArgs].get
        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get

        val sparkDriver = phSparkDriver()
        val redisDriver = new PhRedisDriver()
        val company = redisDriver.getMapValue(uid, "company")
        val singleJobKey = Sercurity.md5Hash(s"$uid$company$ym$mkt")
        val max_path = max_path_obj.p_maxPath + redisDriver.getMapValue(singleJobKey, "max_result_name")
        val max_df = sparkDriver.csv2RDD(max_path, 31.toChar.toString)

        DFArgs(max_df)
    }
}