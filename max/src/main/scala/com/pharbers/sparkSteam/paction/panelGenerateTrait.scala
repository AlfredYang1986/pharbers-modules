package com.pharbers.sparkSteam.paction

import com.pharbers.sparkSteam.paction.actionbase._

object panelGenerateTrait {
    def apply(path : String) : pActionTrait = new panelGenerateTrait(StringArgs(path))
}

class panelGenerateTrait(override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        /**
          * @老齐，把你生成panel的放这里
          */

        NULLArgs
    }
}