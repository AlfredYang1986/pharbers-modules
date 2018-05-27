package com.pharbers.unitTest.action

import com.pharbers.pactions.actionbase._

object writeCheckResultAction {
    def apply(args: MapArgs): pActionTrait = new writeCheckResultAction(args)
}

class writeCheckResultAction(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "result_check"

    override def perform(args : pActionArgs): pActionArgs = {

        NULLArgs
    }
}