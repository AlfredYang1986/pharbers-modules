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
        val maxLst = pr.asInstanceOf[MapArgs].get("phHistoryConditionSearchAction").asInstanceOf[ListArgs].get
                .map(x => x.asInstanceOf[StringArgs].get)

        val sparkDriver = phSparkDriver()
        val redisDriver = new PhRedisDriver()

        val history_df_lst = maxLst.map(singleJobKey =>
            sparkDriver.csv2RDD(max_path_obj.p_maxPath + redisDriver.getMapValue(singleJobKey, "max_result_name_for_search"), 31.toChar.toString)
        )

        DFArgs(unionDataFrameList(history_df_lst))
    }
}
