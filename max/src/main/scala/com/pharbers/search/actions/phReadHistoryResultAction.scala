package com.pharbers.search.actions

import com.pharbers.common.algorithm.phSparkCommonFuncTrait
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver

/**
  * Created by jeorch on 18-5-11.
  */
object phReadHistoryResultAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phReadHistoryResultAction(args)
}

class phReadHistoryResultAction (override val defaultArgs: pActionArgs) extends pActionTrait with phSparkCommonFuncTrait {
    override val name: String = "read_result_action"

    override def perform(pr: pActionArgs): pActionArgs = {
        val uid = defaultArgs.asInstanceOf[MapArgs].get("uid").asInstanceOf[StringArgs].get
        val sparkDriver = phSparkDriver()
        val redisDriver = new PhRedisDriver()
        val company = redisDriver.getMapValue(uid, "company")
        val userJobsKey = Sercurity.md5Hash(s"$uid$company")
        val history_df_lst = redisDriver.getSetAllValue(userJobsKey).map(singleJobKey =>
            sparkDriver.csv2RDD(redisDriver.getMapValue(singleJobKey, "max_path"), 31.toChar.toString)
        ).toList
        DFArgs(unionDataFrameList(history_df_lst))
    }
}
