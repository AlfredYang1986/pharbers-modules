package com.pharbers.panel.nhwa

import scala.reflect.ClassTag
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.nhwa.phNhwaCpaWritable

object phNhwaCalcYMConcretJob {
    def apply[T : ClassTag](args: pActionArgs = NULLArgs): pActionTrait = {
        new phNhwaCalcYMConcretJob[T](args)
    }
}

class phNhwaCalcYMConcretJob[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = "calcYM"
    override def perform(pr : pActionArgs): pActionArgs = {
        val cpaRDD = pr.asInstanceOf[MapArgs].get("cpa").asInstanceOf[RDDArgs[phNhwaCpaWritable]].get.map { iter =>
            iter.getRowKey("YM") + iter.getRowKey("HOSPITAL_CODE") -> 1
        }

        RDDArgs(
            cpaRDD.reduceByKey(_ + _)
                    .filter(_._1.length > 6)
                    .map(iter => iter._1.substring(0, 6) -> 1).reduceByKey(_ + _)
        )
    }
}