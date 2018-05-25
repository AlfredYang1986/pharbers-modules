package com.pharbers.panel.pfizer

import scala.reflect.ClassTag
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.pfizer.phPfizerCpaWritable

/**
  * Created by jeorch on 18-4-18.
  */
object phPfizerCalcYMCpaConcretJob {
    def apply[T : ClassTag](args: pActionArgs = NULLArgs) : pActionTrait = {
        new phPfizerCalcYMCpaConcretJob[T](args)
    }
}

class phPfizerCalcYMCpaConcretJob[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "calcYMWithCpa"
    override def perform(pr : pActionArgs): pActionArgs = {

        val cpaRDD = pr.asInstanceOf[MapArgs].get("cpa").asInstanceOf[RDDArgs[phPfizerCpaWritable]].get.map { iter =>
            iter.getRowKey("YM") + iter.getRowKey("HOSPITAL_CODE") -> 1
        }

        RDDArgs(
            cpaRDD.reduceByKey(_ + _).filter(_._1.length > 6)
                    .map{ iter => iter._1.substring(0, 6) -> 1 }
                    .reduceByKey(_ + _)
        )
    }
}