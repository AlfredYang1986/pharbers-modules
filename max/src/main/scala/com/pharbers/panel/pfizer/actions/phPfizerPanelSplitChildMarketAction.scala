package com.pharbers.panel.pfizer.actions

import com.pharbers.pactions.actionbase._

/**
  * Created by jeorch on 18-4-28.
  */
object phPfizerPanelSplitChildMarketAction {
    def apply(args: MapArgs): pActionTrait = new phPfizerPanelSplitChildMarketAction(args)
}

class phPfizerPanelSplitChildMarketAction (override val defaultArgs : pActionArgs) extends pActionTrait{
    override val name: String = "SplitMarketAction"

    override def perform(args : pActionArgs): pActionArgs = {

        val childMarkets = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get

        //产品标准化 vs IMS_Pfizer_6市场others
        val product_match_file = args.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
        //PACKID生成panel
        val pfc_match_file = args.asInstanceOf[MapArgs].get("pfc_match_file").asInstanceOf[DFArgs].get
        val pfc_filtered = pfc_match_file.filter(s"Market like '${childMarkets}'").distinct()


        //表m1
        val product_match = product_match_file
            .select("min1", "min1_标准", "通用名", "pfc")
            .distinct()

        val spilt_markets_product_match = product_match
            .join(pfc_filtered, product_match("pfc") === pfc_filtered("Pack_ID"))
            .select("min1", "min1_标准")
            .distinct()

        DFArgs(spilt_markets_product_match)
    }

}
