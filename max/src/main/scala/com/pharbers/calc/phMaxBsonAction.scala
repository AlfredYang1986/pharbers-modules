package com.pharbers.calc

import com.pharbers.pactions.actionbase._

object phMaxBsonAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxBsonAction(args)
}

class phMaxBsonAction(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "max_bson_action"
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        NULLArgs
    }

}