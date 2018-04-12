package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._

object sortByFunc {
    def apply(arg_func: pActionArgs, arg_name: String = "sortByFuncJob") : pActionTrait = new sortByFunc(arg_func, arg_name)
}

class sortByFunc[T](override val defaultArgs: pActionArgs,
                    override val name: String) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    //TODO 未实现
    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
//        RDDArgs(args.asInstanceOf[RDDArgs[T]].get.sortBy(_._2))
        NULLArgs
    }

}