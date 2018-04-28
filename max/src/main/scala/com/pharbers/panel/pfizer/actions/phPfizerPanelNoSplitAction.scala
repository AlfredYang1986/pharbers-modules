package com.pharbers.panel.pfizer.actions

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, concat, when}
import org.apache.spark.sql.types.{DoubleType, LongType}

/**
  * Created by jeorch on 18-4-27.
  */
object phPfizerPanelNoSplitAction {
    def apply(args: MapArgs): pActionTrait = new phPfizerPanelNoSplitAction(args)
}

class phPfizerPanelNoSplitAction (override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "SplitMarketAction"
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get

        //通用名市场定义 =>表b0
        val markets_match = args.asInstanceOf[MapArgs].get("markets_match_file").asInstanceOf[DFArgs].get
            .filter(s"Market like '$mkt%'")
        //产品标准化 vs IMS_Pfizer_6市场others
        val product_match_file = args.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
        //表m1
        val product_match = product_match_file
            .select("min1", "min1_标准", "通用名")
            .distinct()
        val markets_product_match = product_match.join(markets_match, product_match("通用名") === markets_match("通用名_原始"))

        DFArgs(markets_product_match)
    }

}