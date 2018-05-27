package com.pharbers.unitTest.action

import com.pharbers.pactions.actionbase._

object resultCheckAction {
    def apply(args: MapArgs): pActionTrait = new resultCheckAction(args)
}

class resultCheckAction(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "result_check"

    override def perform(args : pActionArgs): pActionArgs = {

        NULLArgs
    }
}