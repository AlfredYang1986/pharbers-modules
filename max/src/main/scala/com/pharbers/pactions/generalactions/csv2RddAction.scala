package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver

object csv2RddAction {
    def apply(arg_path : String,
              arg_name : String = "csv2RddJob") : pActionTrait =
        new csv2RddAction(StringArgs(arg_path), arg_name)
}

class csv2RddAction(override val defaultArgs: pActionArgs,
                    override val name: String) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs =
        DFArgs(phSparkDriver().csv2RDD(defaultArgs.asInstanceOf[StringArgs].get, delimiter = 31.toChar.toString))
}