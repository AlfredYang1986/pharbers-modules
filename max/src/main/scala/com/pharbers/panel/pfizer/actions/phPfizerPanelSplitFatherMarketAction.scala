package com.pharbers.panel.pfizer.actions

import com.pharbers.pactions.actionbase._
import com.pharbers.panel.pfizer.phPfizerPanelCommonTrait

/**
  * Created by jeorch on 18-4-28.
  */
object phPfizerPanelSplitFatherMarketAction {
    def apply(args: MapArgs): pActionTrait = new phPfizerPanelSplitFatherMarketAction(args)
}

class phPfizerPanelSplitFatherMarketAction(override val defaultArgs : pActionArgs) extends pActionTrait with phPfizerPanelCommonTrait {
    override val name: String = "SplitMarketAction"

    override def perform(args : pActionArgs): pActionArgs = {

        //在通用名市场定义中包含的此市场
        val current_mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val childMarkets = getChildMarkets(current_mkt)

        //产品标准化 vs IMS_Pfizer_6市场others
        val product_match_file = args.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
        //PACKID生成panel
        val pfc_match_file = args.asInstanceOf[MapArgs].get("pfc_match_file").asInstanceOf[DFArgs].get
        val pfc_filtered = getUnionDF(childMarkets)(x => pfc_match_file.filter(s"Market like '${x}'").distinct())


        //表m1
        val product_match = product_match_file
            .select("min1", "min1_标准", "通用名", "pfc")
            .distinct()

        val spilt_markets_product_match = product_match
            .join(pfc_filtered, product_match("pfc") === pfc_filtered("Pack_ID"), "left").filter("Pack_ID is null")
            .select("min1", "min1_标准")
            .distinct()

        DFArgs(spilt_markets_product_match)
    }

}