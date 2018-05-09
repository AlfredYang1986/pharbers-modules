package com.pharbers.panel.pfizer

import com.pharbers.pactions.actionbase.{NULLArgs, RDDArgs, pActionArgs, pActionTrait}
import com.pharbers.panel.format.input.writable.pfizer.phPfizerCpaWritable

import scala.reflect.ClassTag

/**
  * Created by jeorch on 18-4-18.
  */
object phPfizerCalcYMAction {
    def apply[T : ClassTag](args: pActionArgs = NULLArgs) : pActionTrait = {
        new phPfizerCalcYMAction[T](args)
    }
}

class phPfizerCalcYMAction[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = "calcYM"
    override def perform(pr : pActionArgs): pActionArgs = {
        val cpaRDD = pr.asInstanceOf[RDDArgs[phPfizerCpaWritable]].get.map { iter =>
            iter.getRowKey("YM") + iter.getRowKey("HOSPITAL_CODE") -> 1
        }

        RDDArgs(
            cpaRDD.reduceByKey(_ + _)
                .filter(_._1.length > 6)
                .map(iter => iter._1.substring(0, 6) -> 1).reduceByKey(_ + _)
        )
    }
}