package com.pharbers.paction.funcTrait

import com.pharbers.paction.actionbase.{RDDArgs, StringArgs, pActionArgs, pActionTrait}

import scala.reflect.ClassTag

object saveCurrenResultTrait {
    def apply[T : ClassTag](path : String) : pActionTrait = new saveCurrenResultTrait[T](StringArgs(path))
}

class saveCurrenResultTrait[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val rdd = args.asInstanceOf[RDDArgs[T]].get
        rdd.repartition(1).saveAsTextFile(defaultArgs.asInstanceOf[StringArgs].get)
        args
    }
}
