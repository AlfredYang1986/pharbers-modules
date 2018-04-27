package com.pharbers.calc

import com.pharbers.pactions.actionbase._

object phMaxSplitAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxSplitAction(args)
}

class phMaxSplitAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_split_action"
    override implicit def progressFunc(progress: Double, flag: String) : Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        NULLArgs
    }

}
