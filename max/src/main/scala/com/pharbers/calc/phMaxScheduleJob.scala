package com.pharbers.calc

import com.pharbers.common.algorithm.alTempLog
import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.phSparkDriver

/**
  * Created by jeorch on 18-5-15.
  */
class phMaxScheduleJob extends pActionTrait with phMaxScheduleTrait {

    override val name: String = "phMaxScheduleJob"
    override val defaultArgs: pActionArgs = NULLArgs

    override def perform(pr: pActionArgs): pActionArgs = {
        val hodiernalRddCount = rdd2mongo
        alTempLog(s"Today's RDD count = $hodiernalRddCount")
        val historyRddCount = mongo2rdd
        alTempLog(s"history RDD count = $historyRddCount")
        phSparkDriver().ss.stop()
        defaultArgs
    }

}
