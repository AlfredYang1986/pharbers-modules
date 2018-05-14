package com.pharbers.search.actions

import com.pharbers.common.algorithm.{max_path_obj, phSparkCommonFuncTrait}
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
        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val sparkDriver = phSparkDriver()
        val redisDriver = new PhRedisDriver()
        val userJobsKey = Sercurity.md5Hash(user + company)
        val history_df_lst = redisDriver.getSetAllValue(userJobsKey).map(singleJobKey =>
            sparkDriver.csv2RDD(max_path_obj.p_maxPath + redisDriver.getMapValue(singleJobKey, "max_result_name"), 31.toChar.toString)
        ).toList
        DFArgs(unionDataFrameList(history_df_lst))
    }
}
