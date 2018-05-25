package com.pharbers.unitTest.action

import com.pharbers.pactions.actionbase._

object loadCheckFileAction {
    def apply(args: MapArgs): pActionTrait = new loadCheckFileAction(args)
}

class loadCheckFileAction(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "load_check_file"

    override def perform(args : pActionArgs): pActionArgs = {

        NULLArgs
    }
}