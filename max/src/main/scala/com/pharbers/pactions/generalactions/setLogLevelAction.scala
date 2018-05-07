package com.pharbers.pactions.generalactions

import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

object setLogLevelAction {
    def apply(level: String, arg_name: String = "setLogLevelAction") : pActionTrait =
        new setLogLevelAction(level, arg_name)
}

class setLogLevelAction(level: String, override val name: String) extends pActionTrait {

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        phSparkDriver().sc.setLogLevel(level)

        NULLArgs
    }

    override val defaultArgs : pActionArgs = NULLArgs
    override val progressFactor: Int = 0
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}
}