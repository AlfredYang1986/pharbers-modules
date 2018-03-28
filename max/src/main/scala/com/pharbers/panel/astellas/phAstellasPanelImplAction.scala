package com.pharbers.panel.astellas

import com.pharbers.paction.actionbase._
import com.pharbers.panel.format.input.writable.common.PhXlsxCommonWritable
import com.pharbers.panel.format.input.writable.astellas.{phAstellasCpaWritable, phAstellasGycxWritable}

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
        val cpa = dataMap("cpa").asInstanceOf[RDDArgs[phAstellasCpaWritable]].get
        val gycx = dataMap("gycx").asInstanceOf[RDDArgs[phAstellasGycxWritable]].get
        val product_match = dataMap("product_match_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get
        val markets_match = dataMap("markets_match_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get
        val universe = dataMap("universe_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get


        val c = cpa.count()
        val g = gycx.count()
        val p = product_match.count()
        val m = markets_match.count()
        val u = universe.count()


        val aaa = markets_match.take(100).foreach(println)

//        markets_match.foreach(println)

//        markets_match.foreach{ iter =>
//            val a = iter.getRowKey("")
//            println(a)
//        }



        MapArgs(Map().empty)
    }


}