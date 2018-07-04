package com.pharbers.timer

import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

class TimerTestAction extends pActionTrait {
    override val name: String = "TimerTest"
    override val defaultArgs: pActionArgs = NULLArgs

    override def perform(pr: pActionArgs): pActionArgs = {
        println("TimerTest Success!")
        defaultArgs
    }
}
