package org.apache.spark

import com.pharbers.spark.phSparkDriver
import org.apache.spark.scheduler.SparkListener
import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

object addListenerAction {
    def apply(listener: SparkListener, arg_name: String = "addListenerAction") : pActionTrait =
        new addListenerAction(listener, arg_name)
}

class addListenerAction(listener: SparkListener, override val name: String) extends pActionTrait {

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
//        phSparkDriver().sc.addSparkListener(listener)
        args
    }

    override val defaultArgs: pActionArgs = NULLArgs
    override val progressFactor: Int = 0
    override implicit def progressFunc(progress: Double, flag : String) : Unit = {}
}