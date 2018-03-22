package com.pharbers.sparkSteam.panelgen.actionContainer

import com.pharbers.sparkSteam.paction.actionbase.{NULLArgs, pActionArgs, pActionTrait}

trait pActionContainer extends pActionTrait {

    val actions : List[pActionTrait]

    override def perform(args : pActionArgs)(implicit f : (Double, String) => Unit) : pActionArgs = {
        if (actions.isEmpty) return args
        else midTmpContainer(actions.tail, f).perform(actions.head.perform(args))
    }

    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
}

case class midTmpContainer(override val actions : List[pActionTrait],
                           val f : (Double, String) => Unit) extends pActionContainer {

    override implicit def progressFunc(progress: Double, flag: String) : Unit = f
}
