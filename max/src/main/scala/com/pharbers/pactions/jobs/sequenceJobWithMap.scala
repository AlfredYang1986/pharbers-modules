package com.pharbers.pactions.jobs

import com.pharbers.pactions.actionbase.{MapArgs, NULLArgs, pActionArgs, pActionTrait}

trait sequenceJobWithMap extends pActionTrait {

    val actions : List[pActionTrait]

    override val name: String
    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress: Double, flag: String): Unit = {
        println("progress" + progress)
    }

    override def perform(pr : pActionArgs = MapArgs(Map().empty))
                        (implicit f : (Double, String) => Unit = (_, _) => Unit) : pActionArgs = {

        if (actions.isEmpty) pr
        else {
            val tmp = pr match {
                    case mapArgs: MapArgs => MapArgs(mapArgs.get +
                            (actions.head.name -> actions.head.perform(pr)))
                    case _ => pr
                }

            midMapContainer(actions.tail, f, name).perform(tmp)
        }
    }
}

case class midMapContainer(override val actions : List[pActionTrait],
                           f : (Double, String) => Unit,
                           override val name: String) extends sequenceJobWithMap {

    override implicit def progressFunc(progress: Double, flag: String) : Unit = f
}