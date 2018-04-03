package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.generalactions.memory.PhMemory

object existenceRdd {
    def apply(n : String) : pActionTrait = new existenceRdd(StringArgs(n))
}

class existenceRdd(override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs =
        BooleanArgs(PhMemory.isExist(defaultArgs.asInstanceOf[StringArgs].get))
}