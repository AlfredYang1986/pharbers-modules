package com.pharbers.pactions.jobs

import com.pharbers.pactions.actionbase.{MapArgs, NULLArgs, pActionArgs, pActionTrait}

trait sequenceJobWithMap extends pActionTrait {

    val actions : List[pActionTrait]

    override def perform(pr : pActionArgs = MapArgs(Map().empty))
                        (implicit f : (Double, String) => Unit = (_, _) => Unit) : pActionArgs = {
        if (actions.isEmpty) return pr
        else {
            val tmp = if (pr.isInstanceOf[MapArgs])
                MapArgs(pr.asInstanceOf[MapArgs].get + (actions.head.name -> actions.head.perform(pr)))
            else pr

            midMapContainer(actions.tail, f).perform(tmp)
        }
    }

    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
}

case class midMapContainer(override val actions : List[pActionTrait],
                           f : (Double, String) => Unit) extends sequenceJobWithMap {

    override implicit def progressFunc(progress: Double, flag: String) : Unit = f
}