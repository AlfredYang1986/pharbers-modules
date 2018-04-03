package com.pharbers.pactions.jobs

import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

trait sequenceJob extends pActionTrait {

    val actions : List[pActionTrait]

    override def perform(pr : pActionArgs = NULLArgs)
                        (implicit f : (Double, String) => Unit = (_, _) => Unit) : pActionArgs = {
        if (actions.isEmpty) return pr
        else midTmpContainer(actions.tail, f).perform(actions.head.perform(pr))
    }

    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
}



case class midTmpContainer(override val actions : List[pActionTrait],
                           f : (Double, String) => Unit) extends sequenceJob {

    override implicit def progressFunc(progress: Double, flag: String) : Unit = f
}
