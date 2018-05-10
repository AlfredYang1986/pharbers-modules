package org.apache.spark.listener

import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.phSparkDriver
import org.apache.spark.scheduler.SparkListener

object addListenerAction {
    def apply(listener: SparkListener, arg_name: String = "addListenerAction"): pActionTrait =
        new addListenerAction(listener, arg_name)
}

class addListenerAction(listener: SparkListener,
                        override val name: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override def perform(args: pActionArgs): pActionArgs = {
        phSparkDriver().sc.addSparkListener(listener)
        args
    }
}