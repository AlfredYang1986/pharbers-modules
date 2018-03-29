package com.pharbers.panel.astellas

import com.pharbers.paction.actionbase._
import com.pharbers.panel.format.input.writable.common.PhXlsxCommonWritable
import com.pharbers.panel.format.input.writable.astellas.{phAstellasCpaWritable, phAstellasGycxWritable, phAstellasMarketsMatchWritable}

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
        // 1. 读入cpa原始数据
        val dataMap = pr.asInstanceOf[MapArgs].get
        val cpa = dataMap("cpa").asInstanceOf[RDDArgs[phAstellasCpaWritable]].get
        val gycx = dataMap("gycx").asInstanceOf[RDDArgs[phAstellasGycxWritable]].get
        val markets_match = dataMap("markets_match_file").asInstanceOf[RDDArgs[phAstellasMarketsMatchWritable]].get.map{ iter =>
            (iter.getRowKey("MOLE_NAME"), iter.getRowKey("MARKET"))
        }
        val product_match = dataMap("product_match_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get
        val universe = dataMap("universe_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get









        val cpa1 = cpa.map{ iter =>
            (iter.getRowKey("ATC_CODE"), iter.getRowKey("PRODUCT_NAME"), iter.getRowKey("PRODUCT_NAME_NOTE"), iter.getRowKey("MOLE_NAME"))
        }
//        cpa1.take(10).foreach(println)

        //3. GYCX匹配市场
        val gycx1 = gycx.map{ iter =>
            iter.getRowKey("MOLE_NAME") -> iter
        }.join(markets_match).map{iter =>
            iter._1 -> (iter._2._1, iter._2._2)
        }



        gycx1.take(10).foreach(println)








//        cpa.map(_.getRowKey("PROVINCES") -> 1).take(100).foreach(println)

//        markets_match.foreach(println)

//        markets_match.foreach{ iter =>
//            val a = iter.getRowKey("")
//            println(a)
//        }



        NULLArgs
    }


}