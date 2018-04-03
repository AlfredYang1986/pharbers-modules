package com.pharbers.panel.nhwa

import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.astellas.phAstellasCpaWritable

import scala.reflect.ClassTag

object phNhwaCalcYMImplAction  {
    def apply[T : ClassTag](name: String = "", args : pActionArgs = NULLArgs) : pActionTrait = {
        val temp = new phNhwaCalcYMImplAction[T](args)
        temp.name = name
        temp
    }
}

class phNhwaCalcYMImplAction[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(pr : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val cpaRDD = pr.get.asInstanceOf[RDDArgs[phAstellasCpaWritable]].get.map { iter =>
            iter.getRowKey("YM") -> iter.getRowKey("HOSPITAL_CODE")
        }

        RDDArgs(
            cpaRDD.map{ iter => iter._1 -> 1 }.reduceByKey(_ + _)
        )
    }
}