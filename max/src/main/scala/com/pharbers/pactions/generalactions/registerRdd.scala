package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.generalactions.memory.{phMemory, phMemoryArgs}

object registerRdd {
    def apply(arg_key: String, arg_name: String = "existenceJob")
             (implicit company: phMemoryArgs): pActionTrait =
        new existenceRdd(StringArgs(arg_key), arg_name)
}

class registerRdd(override val defaultArgs: pActionArgs,
                   override val name: String)(implicit company: phMemoryArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs =
        BooleanArgs(phMemory.isExist(defaultArgs.asInstanceOf[StringArgs].get))
}