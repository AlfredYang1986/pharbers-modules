package com.pharbers.unitTest

import com.pharbers.unitTest.action._
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}

case class resultCheckJob(args: Map[String, String]) extends sequenceJobWithMap {
    override val name: String = "result_check_job"

    val df = MapArgs(args.map(x => x._1 -> StringArgs(x._2)))

    override val actions: List[pActionTrait] = {
        executeMaxAction(df) ::
        loadCheckFileAction(df) ::
        resultCheckAction(df) ::
        writeCheckResultAction(df) ::
        Nil
    }
}
