package com.pharbers.sparkSteam.paction

import com.pharbers.sparkSteam.paction.actionbase._

object calcYMTrait {
    def apply(path : String) : pActionTrait = new calcYMTrait(StringArgs(path))
}

class calcYMTrait(override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        /**
          * @老齐，生成年月的放这里
          */

        NULLArgs
    }
}
