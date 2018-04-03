package com.pharbers.pactions.generalactions

import scala.reflect.ClassTag
import com.pharbers.pactions.actionbase.{RDDArgs, StringArgs, pActionArgs, pActionTrait}

object saveCurrenResultAction {
    def apply[T : ClassTag](path : String) : pActionTrait = new saveCurrenResultAction[T](StringArgs(path))
}

class saveCurrenResultAction[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val rdd = args.asInstanceOf[RDDArgs[T]].get
        rdd.repartition(1).saveAsTextFile(defaultArgs.asInstanceOf[StringArgs].get)
        args
    }
}
