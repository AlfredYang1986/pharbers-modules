package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.generalactions.memory.PhMemory

object existenceRdd {
    def apply(arg_key: String,
              arg_name: String = "existenceJob") : pActionTrait =
        new existenceRdd(StringArgs(arg_key), arg_name)
}

class existenceRdd(override val defaultArgs: pActionArgs,
                   override val name: String) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs =
        BooleanArgs(PhMemory.isExist(defaultArgs.asInstanceOf[StringArgs].get))
}