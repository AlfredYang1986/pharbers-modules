package com.pharbers.calc

import com.pharbers.pactions.actionbase._

object phMaxSplitAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxSplitAction(args)
}

class phMaxSplitAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_split_action"
    override def perform(pr: pActionArgs): pActionArgs = NULLArgs
}