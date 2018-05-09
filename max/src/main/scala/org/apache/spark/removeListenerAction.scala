package org.apache.spark

import com.pharbers.spark.phSparkDriver
import org.apache.spark.scheduler.SparkListener
import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

object removeListenerAction {
    def apply(listener: SparkListener, arg_name: String = "removeListenerAction"): pActionTrait =
        new removeListenerAction(listener, arg_name)
}

class removeListenerAction(listener: SparkListener, override val name: String) extends pActionTrait {

    override def perform(args: pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        phSparkDriver().sc.listenerBus.removeListener(listener)
        args
    }

    override val defaultArgs: pActionArgs = NULLArgs
    override val progressFactor: Int = 0
    override implicit def progressFunc(progress: Double, flag : String) : Unit = {}
}