package com.pharbers.calc

import com.pharbers.pactions.actionbase._

object phMaxCalcJob {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxCalcJob(args)
}

class phMaxCalcJob(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "max_group_job"
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        NULLArgs
    }

}