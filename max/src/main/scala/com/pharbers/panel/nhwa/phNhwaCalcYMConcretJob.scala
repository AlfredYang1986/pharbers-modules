package com.pharbers.panel.nhwa

import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.nhwa.phNhwaCpaWritable

import scala.reflect.ClassTag

object phNhwaCalcYMConcretJob  {
    def apply[T : ClassTag](name: String = "", args : pActionArgs = NULLArgs) : pActionTrait = {
        val temp = new phNhwaCalcYMConcretJob[T](args)
//        temp.name = name
        temp
    }
}

class phNhwaCalcYMConcretJob[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = ""
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(pr : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val cpaRDD = pr.asInstanceOf[RDDArgs[phNhwaCpaWritable]].get.map { iter =>
            iter.getRowKey("YM") + iter.getRowKey("HOSPITAL_CODE") -> 1
        }

        RDDArgs(
            cpaRDD.reduceByKey(_ + _).map (iter => iter._1.substring(0, 6) -> 1).reduceByKey(_ + _)
        )
    }
}