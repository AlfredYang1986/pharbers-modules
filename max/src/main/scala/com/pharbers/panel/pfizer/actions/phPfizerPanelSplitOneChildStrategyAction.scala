package com.pharbers.panel.pfizer.actions

import com.pharbers.pactions.actionbase._

/**
  * Created by jeorch on 18-4-27.
  */
object phPfizerPanelSplitOneChildStrategyAction {
    def apply(args: MapArgs): pActionTrait = new phPfizerPanelSplitOneChildStrategyAction(args)
}

class phPfizerPanelSplitOneChildStrategyAction(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "noSplitMarketAction"
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        //在通用名市场定义中包含的此市场
        val current_mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val childMarket = getChildMarket(current_mkt)
        //通用名市场定义 =>表b0
        val markets_match = args.asInstanceOf[MapArgs].get("markets_match_file").asInstanceOf[DFArgs].get
            .filter(s"Market like '$current_mkt%'")
        //产品标准化 vs IMS_Pfizer_6市场others
        val product_match_file = args.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
        //PACKID生成panel
        val pfc_match_file = args.asInstanceOf[MapArgs].get("pfc_match_file").asInstanceOf[DFArgs].get
            .filter(s"Market like '${childMarket}'")
            .distinct()
        //表m1
        val product_match = product_match_file
            .select("min1", "min1_标准", "通用名", "pfc")
            .distinct()
        val markets_product_match = product_match.join(markets_match, product_match("通用名") === markets_match("通用名_原始"))

        val spilt_markets_product_match = markets_product_match
            .join(pfc_match_file, markets_product_match("pfc") === pfc_match_file("Pack_ID"), "left").filter("Pack_ID is null")
            .drop(pfc_match_file("Pack_ID"))
            .drop(pfc_match_file("Market"))

        DFArgs(spilt_markets_product_match)
    }

    def getChildMarket(curr_mkt: String) = curr_mkt match {
        case "PAIN_other" => "PAIN_C"
        case "HTN" => "HTN2"
        case "AI_W" => "PAIN_C"
        case _ => throw new Exception(s"Error in phPfizerPanelSplitOneChildStrategyAction. Market=${curr_mkt} has no child market!")
    }

}
