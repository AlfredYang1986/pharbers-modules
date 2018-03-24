package com.pharbers.paction.actionContainer

import com.pharbers.paction.actionbase.{MapArgs, NULLArgs, pActionArgs, pActionTrait}

trait pActionContainer extends pActionTrait {

    val actions : List[pActionTrait]

    override def perform(pr : pActionArgs)(implicit f : (Double, String) => Unit = (_, _) => Unit) : pActionArgs = {
        if (actions.isEmpty) return pr
        else midTmpContainer(actions.tail, f).perform(actions.head.perform(pr))
    }

    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
}

trait pMapActionContainer extends pActionTrait {

    val actions : List[pActionTrait]

    override def perform(pr : pActionArgs)(implicit f : (Double, String) => Unit) : pActionArgs = {
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

case class midTmpContainer(override val actions : List[pActionTrait],
                           f : (Double, String) => Unit) extends pActionContainer {

    override implicit def progressFunc(progress: Double, flag: String) : Unit = f
}

case class midMapContainer(override val actions : List[pActionTrait],
                           f : (Double, String) => Unit) extends pMapActionContainer {

    override implicit def progressFunc(progress: Double, flag: String) : Unit = f
}
