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
        val markets_match = dataMap("markets_match_file").asInstanceOf[RDDArgs[phAstellasMarketsMatchWritable]].get
        val product_match = dataMap("product_match_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get
        val universe = dataMap("universe_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get









        //2.商品名备注替换商品名
        val cpa1 = cpa.map{ iter =>
            if (iter.getRowKey("PRODUCT_NAME_NOTE") != "")
                iter.getRowKey("PRODUCT_NAME_NOTE")
            else if(iter.getRowKey("PRODUCT_NAME") == "")
                iter.getRowKey("MOLE_NAME")
            else
                iter.getRowKey("PRODUCT_NAME")
        }



        val gycx1 = gycx.map{ iter =>
            val moleName = if (iter.getRowKey("PRODUCT_NAME") == "")
                iter.getRowKey("MOLE_NAME")
            else
                iter.getRowKey("PRODUCT_NAME")

            (
                moleName,
                iter.getRowKey("MOLE_NAME")
            )
        }

        gycx1.take(10).foreach(println)


        //3. GYCX匹配市场
//        val markets_match1 = markets_match.map{ iter =>
//            (iter.getRowKey("MOLE_NAME"), iter.getRowKey("MARKET"))
//        }
//
//        val gycx2 = gycx1.join(markets_match1)
//
//        gycx2.take(10).foreach(println)








//        cpa.map(_.getRowKey("PROVINCES") -> 1).take(100).foreach(println)

//        markets_match.foreach(println)

//        markets_match.foreach{ iter =>
//            val a = iter.getRowKey("")
//            println(a)
//        }



        NULLArgs
    }


}