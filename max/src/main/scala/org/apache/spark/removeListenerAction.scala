package org.apache.spark

import com.pharbers.spark.phSparkDriver
import org.apache.spark.scheduler.SparkListener
import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

object removeListenerAction {
    def apply(listener: SparkListener, arg_name: String = "removeListenerAction"): pActionTrait =
        new removeListenerAction(listener, arg_name)
}

class removeListenerAction(listener: SparkListener, override val name: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override def perform(args: pActionArgs): pActionArgs = {
        phSparkDriver().sc.listenerBus.removeListener(listener)
        args
    }
}