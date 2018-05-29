package com.pharbers.panel.astellas

import scala.reflect.ClassTag
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.astellas.phAstellasCpaWritable

object phAstellasCalcYMCpaConcretJob  {
    def apply[T : ClassTag](args: pActionArgs = NULLArgs) : pActionTrait = {
        new phAstellasCalcYMCpaConcretJob[T](args)
    }
}

class phAstellasCalcYMCpaConcretJob[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = "calcYMWithCpa"
    override def perform(pr : pActionArgs): pActionArgs = {

        val cpaRDD = pr.asInstanceOf[MapArgs].get("cpa").asInstanceOf[RDDArgs[phAstellasCpaWritable]].get.map { iter =>
            iter.getRowKey("YM") + iter.getRowKey("HOSPITAL_CODE") -> 1
        }

        RDDArgs(
            cpaRDD.reduceByKey(_ + _).filter(_._1.length > 6)
                    .map{ iter => iter._1.substring(0, 6) -> 1 }
                    .reduceByKey(_ + _)
        )
    }
}