package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver

object csv2RddAction {
    def apply(path : String , n : String) : pActionTrait = {
        val tmp = new csv2RddAction(StringArgs(path))
        tmp.name = n
        tmp
    }
}

class csv2RddAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs =
        DFArgs(phSparkDriver().csv2RDD(defaultArgs.asInstanceOf[StringArgs].get, delimiter = 31.toChar.toString))
}