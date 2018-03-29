package com.pharbers.panel.astellas

import com.pharbers.paction.actionbase._
import com.pharbers.panel.format.input.writable.astellas._
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
        // 1. 读入cpa原始数据 2.在Writable进行预处理
        val dataMap = pr.asInstanceOf[MapArgs].get
        val cpa = dataMap("cpa").asInstanceOf[RDDArgs[phAstellasCpaWritable]].get
        val gycx = dataMap("gycx").asInstanceOf[RDDArgs[phAstellasGycxWritable]].get
        val markets_match = dataMap("markets_match_file").asInstanceOf[RDDArgs[phAstellasMarketsMatchWritable]].get
        val product_match = dataMap("product_match_file").asInstanceOf[RDDArgs[phAstellasProductMatchWritable]].get
        val universe = dataMap("universe_file").asInstanceOf[RDDArgs[PhXlsxCommonWritable]].get



        val cpa1 = cpa.map{ iter =>
            iter.getRowKey("min1") -> iter
        }// cpa.min1 -> cpaRDD
//        cpa1.take(10).foreach(println)

        val markets_match1 = markets_match.map{ iter =>
            (iter.getRowKey("MOLE_NAME"), iter.getRowKey("MARKET"))
        }// mkt.MOLE_NAME -> mkt.MARKET
//        markets_match1.take(10).foreach(println)



        //3. GYCX匹配市场
        val gycx1 = gycx.map{ iter =>
            iter.getRowKey("MOLE_NAME") -> iter
        }.join(markets_match1).map{iter =>
            iter._2._1.getRowKey("min1") -> (iter._2._1, iter._2._2)
        }// gycx.min1 -> (gycxRDD, gycx.market)
//        gycx1.take(10).foreach(println)



        //4. min1匹配
        val product_match1 = product_match.map{ iter =>
            iter.getRowKey("min0") -> iter
        }// product.min0 -> productRDD
//        product_match1.take(10).foreach(println)



        //5. 修改市场
        val cpa2 = cpa1.join(product_match1) // min1 -> (cpaRDD, productRDD)
                .map { iter =>

            val market = if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "软膏剂")
                "普特彼市场"
            else if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "软膏剂")
                "普特可复市场"
            else
                iter._2._1.getRowKey("MARKET")

            market -> iter._2
        } // market -> (cpaRDD, productRDD)
//        cpa2.take(10).foreach(println)

        val gycx2 = gycx1.join(product_match1) // min1 -> ((gycxRDD, gycx.market), productRDD)
                .map { iter =>

            val market = if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "软膏剂")
                "普特彼市场"
            else if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "软膏剂")
                "普特可复市场"
            else
                iter._2._1._2

            market -> (iter._2._1._1, iter._2._2)
        } // market -> (gycxRDD, productRDD)
//        gycx2.take(10).foreach(println)


        //6. 删除一些数据










        NULLArgs
    }


}