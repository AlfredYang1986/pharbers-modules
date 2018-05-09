package com.pharbers.pactions.generalactions

import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase.{RDDArgs, StringArgs, pActionArgs, pActionTrait}

/**
  * Created by jeorch on 18-4-26.
  */
object txt2RDDAction {
    def apply(arg_path: String, name: String = "txt2RddJob"): pActionTrait =
        new txt2RDDAction(StringArgs(arg_path), name)
}

class txt2RDDAction(override val defaultArgs: pActionArgs,
                    override val name: String) extends pActionTrait {

    override def perform(args: pActionArgs): pActionArgs =
        RDDArgs(phSparkDriver().sc.textFile(defaultArgs.asInstanceOf[StringArgs].get))
}