package com.pharbers.panel.pfizer.actions

import com.pharbers.pactions.actionbase._
import org.apache.spark.sql.DataFrame

/**
  * Created by jeorch on 18-4-28.
  */
object phPfizerPanelSplitMarketAction {
    def apply(args: MapArgs): pActionTrait = new phPfizerPanelSplitMarketAction(args)
}

class phPfizerPanelSplitMarketAction(override val defaultArgs : pActionArgs) extends pActionTrait{
    override val name: String = "SplitMarketAction"
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        //在通用名市场定义中包含的此市场
        val current_mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val childMarkets = getChildMarkets(current_mkt)
        //通用名市场定义 =>表b0
        val markets_match = args.asInstanceOf[MapArgs].get("markets_match_file").asInstanceOf[DFArgs].get
            .filter(s"Market like '$current_mkt%'")
        //产品标准化 vs IMS_Pfizer_6市场others
        val product_match_file = args.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
        //PACKID生成panel
        val pfc_match_file = args.asInstanceOf[MapArgs].get("pfc_match_file").asInstanceOf[DFArgs].get
        val pfc_filtered = getFilterPfc(childMarkets, pfc_match_file)


        //表m1
        val product_match = product_match_file
            .select("min1", "min1_标准", "通用名", "pfc")
            .distinct()
        val markets_product_match = product_match.join(markets_match, product_match("通用名") === markets_match("通用名_原始"))

        val spilt_markets_product_match = markets_product_match
            .join(pfc_filtered, markets_product_match("pfc") === pfc_filtered("Pack_ID"), "left").filter("Pack_ID is null")
            .drop(pfc_filtered("Pack_ID"))
            .drop(pfc_filtered("Market"))

        DFArgs(spilt_markets_product_match)
    }

    def getFilterPfc(lstMkt: List[String], originPfcDF: DataFrame):DataFrame = {
        lstMkt match {
            case Nil => throw new Exception("Error in phPfizerPanelSplitOneChildStrategyAction.EmptyList")
            case head::Nil => originPfcDF.filter(s"Market like '${head}'").distinct()
            case head::tail => originPfcDF.filter(s"Market like '${head}'").distinct()
                .union(getFilterPfc(tail,originPfcDF))
        }
    }

    /**
      * 之后如果出现新的拆分市场,只需维护getChildMarket函数即可
      * @param curr_mkt
      * @return
      */

    def getChildMarkets(curr_mkt: String): List[String] = curr_mkt match {
        case "AI_R_other" => "AI_D"::"ZYVOX"::Nil
        case "PAIN_other" => "PAIN_C"::Nil
        case "HTN" => "HTN2"::Nil
        case "AI_W" => "PAIN_C"::Nil
        case _ => throw new Exception(s"Error in phPfizerPanelSplitOneChildStrategyAction. Market=${curr_mkt} has no child market!")
    }

}