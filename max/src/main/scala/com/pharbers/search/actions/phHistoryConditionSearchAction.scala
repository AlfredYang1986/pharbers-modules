package com.pharbers.search.actions

import com.pharbers.common.algorithm.phSparkCommonFuncTrait
import com.pharbers.pactions.actionbase._

/**
  * Created by jeorch on 18-5-14.
  */
object phHistoryConditionSearchAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phHistoryConditionSearchAction(args)
}

class phHistoryConditionSearchAction(override val defaultArgs: pActionArgs) extends pActionTrait with phSparkCommonFuncTrait {

    override val name: String = "phHistoryConditionSearchAction"

    override def perform(pr: pActionArgs): pActionArgs = {

        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val ym_condition = defaultArgs.asInstanceOf[MapArgs].get("ym_condition").asInstanceOf[StringArgs].get
        val historyDF = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get

        val filteredMktDF = mkt match {
            case "" => historyDF
            case _ => {
                historyDF.filter(s"MARKET like '${mkt}'")
            }
        }

        val filteredYM = ym_condition match {
            case "-" => filteredMktDF
            case _ => {
                val ym_start = ym_condition.split("-")(0)
                val ym_end = ym_condition.split("-")(1)
                filteredMktDF.filter(s"Date gte '${ym_start}'").filter(s"Date lte '${ym_end}'")
            }
        }
        DFArgs(filteredYM)
    }

}
