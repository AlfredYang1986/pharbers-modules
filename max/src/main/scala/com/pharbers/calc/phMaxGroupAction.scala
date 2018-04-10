package com.pharbers.calc

import com.pharbers.pactions.actionbase._

object phMaxGroupAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxGroupAction(args)
}

class phMaxGroupAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_group_action"
    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr : pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {
        val panelDF = pr.asInstanceOf[MapArgs].get("panel_data").asInstanceOf[DFArgs].get
        val universeDF = pr.asInstanceOf[MapArgs].get("universe_data").asInstanceOf[DFArgs].get
        val groupByPanel = panelDF.groupBy("Date", "Prod_CNAME")
        panelDF.show(false)
        universeDF.show(false)
        NULLArgs
    }

}