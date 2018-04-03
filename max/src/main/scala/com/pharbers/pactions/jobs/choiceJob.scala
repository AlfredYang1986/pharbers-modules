package com.pharbers.pactions.jobs

import com.pharbers.pactions.actionbase.{BooleanArgs, NULLArgs, pActionArgs, pActionTrait}

trait choiceJob extends pActionTrait {

    val actions : List[pActionTrait]

    override def perform(pr : pActionArgs = NULLArgs)
                        (implicit f : (Double, String) => Unit = (_, _) => Unit) : pActionArgs = {

        assert(actions.length == 3)
        val result = actions.head.perform(pr)
        if (result.asInstanceOf[BooleanArgs].get) {
            midTmpContainer(actions.tail.head :: Nil, f).perform(pr)
        } else midTmpContainer(actions.tail.tail.head :: Nil, f).perform(pr)
    }

    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
}