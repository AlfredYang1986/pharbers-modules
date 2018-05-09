package com.pharbers.pactions.jobs

import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

trait sequenceJob extends pActionTrait {

    val actions : List[pActionTrait]

    override val name: String
    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String) : Unit = Unit

    override def perform(pr : pActionArgs = NULLArgs)
                        (implicit f : (Double, String) => Unit = (_, _) => Unit) : pActionArgs = {
        if (actions.isEmpty) pr
        else midTmpContainer(actions.tail, f, name).perform(actions.head.perform(pr))
    }

}

case class midTmpContainer(override val actions : List[pActionTrait],
                           f : (Double, String) => Unit,
                           override val name: String) extends sequenceJob {

    override implicit def progressFunc(progress: Double, flag: String) : Unit = f
}
