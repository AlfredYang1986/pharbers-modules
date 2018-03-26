package com.pharbers.panel.commonAction

import scala.reflect.ClassTag
import com.pharbers.paction.actionbase._

object calcYMAction  {
    def apply[T : ClassTag](args : pActionArgs) : pActionTrait = new calcYMAction(args)
}

class calcYMAction[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait with java.io.Serializable {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(pr : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val rdd = pr.asInstanceOf[RDDArgs[T]].get
        RDDArgs(
            rdd.map { iter =>
                val tmp = defaultArgs.asInstanceOf[MapArgs].get

                (tmp("fym").asInstanceOf[SingleArgFuncArgs[T, String]].get(iter).toString +
                        tmp("fc").asInstanceOf[SingleArgFuncArgs[T, String]].get(iter).toString) -> 1

            }.reduceByKey(_ + _).map (x => (x._1.substring(0, 6), 1)).reduceByKey(_ + _)
        )
    }
}