package com.pharbers.calc

import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

/**
  * Created by jeorch on 18-4-10.
  */
object phMaxCalcAction {
    def apply(uid: String,name: String): pActionTrait = new phMaxCalcAction(uid, name)
}

class phMaxCalcAction(uid: String, override val name: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        defaultArgs
    }
}
