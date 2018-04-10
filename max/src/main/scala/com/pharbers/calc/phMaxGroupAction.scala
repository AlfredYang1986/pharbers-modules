package com.pharbers.calc

import com.pharbers.pactions.actionbase._

/**
  * Created by jeorch on 18-4-9.
  */
object phMaxGroupAction {
    def apply(panel: String, name: String): pActionTrait = new phMaxGroupAction(panel, name)
}

class phMaxGroupAction(panel: String, override val name: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        val panelDF = pr.asInstanceOf[MapArgs].get("csv2RddJob").asInstanceOf[DFArgs].get
        val groupByPanel = panelDF.groupBy("Date", "Prod_CNAME")
        defaultArgs
    }
}
