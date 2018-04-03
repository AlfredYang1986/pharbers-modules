package com.pharbers.pactions.jobs

import com.pharbers.pactions.actionbase.{BooleanArgs, NULLArgs, pActionArgs, pActionTrait}

trait choiceJob extends pActionTrait {

    val actions : List[pActionTrait]

    override def perform(pr : pActionArgs = NULLArgs)
                        (implicit f : (Double, String) => Unit = (_, _) => Unit) : pActionArgs = {

        assert(actions.length == 3)
        val result = actions.head.perform(defaultArgs)
        if (result.asInstanceOf[BooleanArgs].get) {
            midTmpContainer(actions.tail.tail.head :: Nil, f).perform(pr)       // false
        } else midTmpContainer(actions.tail.head :: Nil, f).perform(pr)         // true
    }

    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
}