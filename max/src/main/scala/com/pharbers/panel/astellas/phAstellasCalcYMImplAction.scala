package com.pharbers.panel.astellas

import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.astellas.{phAstellasCpaWritable, phAstellasGycxWritable}

object phAstellasCalcYMImplAction  {
    def apply(name: String, args : pActionArgs = NULLArgs) : pActionTrait = {
        val temp = new phAstellasCalcYMImplAction(args)
//        temp.name = name
        temp
    }
}

class phAstellasCalcYMImplAction(override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = ""

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(pr : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val dataMap = pr.asInstanceOf[MapArgs].get

        val cpaRDD = dataMap("cpa").asInstanceOf[RDDArgs[phAstellasCpaWritable]].get.map { iter =>
            iter.getRowKey("YM") -> iter.getRowKey("HOSPITAL_CODE")
        }

        val gycxRDD = dataMap("gycx").asInstanceOf[RDDArgs[phAstellasGycxWritable]].get.map { iter =>
            iter.getRowKey("YM") -> iter.getRowKey("HOSPITAL_CODE")
        }

        RDDArgs(
            (cpaRDD union gycxRDD).map{ iter =>
                iter._1 -> 1
            }.reduceByKey(_ + _)
        )
    }
}