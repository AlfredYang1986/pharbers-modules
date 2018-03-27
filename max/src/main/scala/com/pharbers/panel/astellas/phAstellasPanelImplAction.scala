package com.pharbers.panel.astellas

import com.pharbers.paction.actionbase._
import com.pharbers.panel.format.input.writable.astellas.PhXlsxCpaWritable
import com.pharbers.panel.format.input.writable.common.PhXlsxCommonWritable

/**
  * Created by spark on 18-3-27.
  */
object phAstellasPanelImplAction  {
    def apply(company: String, ym: List[String], mkt: String) : pActionTrait = new phAstellasPanelImplAction(company, ym, mkt)
}

class phAstellasPanelImplAction(company: String, ym: List[String], mkt: String) extends pActionTrait with java.io.Serializable {

    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(pr : pActionArgs)(implicit f : (Double, String) => Unit) : pActionArgs = {
        val dataMap = pr.asInstanceOf[MapArgs].get
        val cpa = dataMap("cpa").asInstanceOf[RDDArgs[PhXlsxCpaWritable]].get
        val gycx = dataMap("gycx").asInstanceOf[RDDArgs[PhXlsxCpaWritable]].get
        val product_match = dataMap("product_match_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get
        val markets_match = dataMap("markets_match_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get
        val universe = dataMap("universe_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get


        val c = cpa.count()
        val p = product_match.count()
        val m = markets_match.count()
        val u = universe.count()
//        cpa.take(20).foreach(println)
//        val e = markets_match.take(10)


//        val e = markets_match.map { iter =>
//            iter.getRowKey("竞品市场") -> 1
//
//        }.reduceByKey(_ + _)

        markets_match.foreach{ iter =>
            val a = iter.getRowKey("")
            println(a)
        }




        NULLArgs
    }


}