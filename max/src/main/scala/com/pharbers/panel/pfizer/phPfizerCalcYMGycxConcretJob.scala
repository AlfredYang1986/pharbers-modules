package com.pharbers.panel.pfizer

import scala.reflect.ClassTag
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.pfizer.phPfizerGycxWritable

object phPfizerCalcYMGycxConcretJob {
    def apply[T : ClassTag](args: pActionArgs = NULLArgs) : pActionTrait = {
        new phPfizerCalcYMGycxConcretJob[T](args)
    }
}

class phPfizerCalcYMGycxConcretJob[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = "calcYMWithGycx"
    override def perform(pr : pActionArgs): pActionArgs = {

        val gycRDD = pr.asInstanceOf[MapArgs].get("gycx").asInstanceOf[RDDArgs[phPfizerGycxWritable]].get.map { iter =>
            iter.getRowKey("YM") + iter.getRowKey("HOSPITAL_CODE") -> 1
        }

        RDDArgs(
            gycRDD.reduceByKey(_ + _).filter(_._1.length > 6)
                    .map{ iter => iter._1.substring(0, 6) -> 1 }
                    .reduceByKey(_ + _)
        )
    }
}