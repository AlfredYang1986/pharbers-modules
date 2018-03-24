package com.pharbers.sparkSteam.paction

import com.pharbers.sparkSteam.paction.actionbase._

import scala.reflect.ClassTag

object calcYMTrait2  {
    def apply[T : ClassTag](args : pActionArgs) : pActionTrait = new calcYMTrait2(args)
}

class calcYMTrait2[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait with java.io.Serializable {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val rdd = args.asInstanceOf[RDDArgs[T]].get
        RDDArgs(
        rdd.map { iter =>
            val tmp = defaultArgs.asInstanceOf[MapArgs].get

            (tmp.get("fy").get.asInstanceOf[SingleArgFuncArgs[T, String]].func(iter).toString +
                tmp.get("fm").get.asInstanceOf[SingleArgFuncArgs[T, String]].func(iter).toString +
                    tmp.get("fc").get.asInstanceOf[SingleArgFuncArgs[T, String]].func(iter).toString) -> 1

        }.reduceByKey(_ + _).map (x => (x._1.substring(0, 6), 1)).reduceByKey(_ + _))
    }
}