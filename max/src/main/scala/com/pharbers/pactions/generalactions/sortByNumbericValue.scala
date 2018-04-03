package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._

object sortByFunc {
    def apply(func : pActionArgs) : pActionTrait = new sortByFunc(func)
}

class sortByFunc[T](override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
//        RDDArgs(args.asInstanceOf[RDDArgs[T]].get.sortBy(_._2))
        NULLArgs
    }

}