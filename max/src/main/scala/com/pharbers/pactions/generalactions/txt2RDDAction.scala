package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase.{RDDArgs, StringArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.phSparkDriver

/**
  * Created by jeorch on 18-4-26.
  */
object txt2RDDAction {
    def apply(arg_path: String, name: String = "txt2RddJob"): pActionTrait =
        new txt2RDDAction(StringArgs(arg_path), name)
}

class txt2RDDAction(override val defaultArgs: pActionArgs, override val name: String) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs =
        RDDArgs(phSparkDriver().sc.textFile(defaultArgs.asInstanceOf[StringArgs].get))
}