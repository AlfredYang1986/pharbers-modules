package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver

object csv2DFAction {
    def apply(arg_path: String, delimiter: String = 31.toChar.toString,
              arg_name: String = "csv2RddJob"): pActionTrait =
        new csv2DFAction(StringArgs(arg_path), delimiter, arg_name)
}

class csv2DFAction(override val defaultArgs: pActionArgs,
                   delimiter: String,
                   override val name: String) extends pActionTrait {

    override def perform(args: pActionArgs): pActionArgs =
        DFArgs(phSparkDriver().csv2RDD(defaultArgs.asInstanceOf[StringArgs].get, delimiter))
}