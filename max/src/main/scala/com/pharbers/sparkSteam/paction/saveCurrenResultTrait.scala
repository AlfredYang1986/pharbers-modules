package com.pharbers.sparkSteam.paction

import com.pharbers.panel.format.input.nhwa.PhExcelNhwaFormat
import com.pharbers.panel.format.input.writable.PhExcelWritable
import com.pharbers.sparkSteam.paction.actionbase._

import scala.reflect.ClassTag

object saveCurrenResultTrait {
    def apply[T : ClassTag](path : String) : pActionTrait = new saveCurrenResultTrait[T](StringArgs(path))
}

class saveCurrenResultTrait[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val rdd = args.asInstanceOf[RDDArgs[T]].get //.asInstanceOf[RDDArgs[PhExcelWritable]].get
//        rdd.foreach(x => println(x.toString))
        rdd.saveAsTextFile(defaultArgs.asInstanceOf[StringArgs].get)
        args
    }
}
