package com.pharbers.pactions.generalactions

import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

object setLogLevelAction {
    def apply(level: String, arg_name: String = "setLogLevelAction") : pActionTrait =
        new setLogLevelAction(level, arg_name)
}

class setLogLevelAction(level: String, override val name: String) extends pActionTrait {
    override val defaultArgs : pActionArgs = NULLArgs

    override def perform(args : pActionArgs): pActionArgs = {
        phSparkDriver().sc.setLogLevel(level)
        NULLArgs
    }
}