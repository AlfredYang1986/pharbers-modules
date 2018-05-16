package com.pharbers.search.actions

import com.pharbers.common.algorithm.phSparkCommonFuncTrait
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity

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

        val filteredYMDF = ym_condition match {
            case "" => filteredMktDF
            case "-" => filteredMktDF
            case _ => {
                val ym_start = ym_condition.split("-")(0).toInt
                val ym_end = ym_condition.split("-")(1).toInt
                filteredMktDF.filter(filteredMktDF("Date").gt(ym_start-1)).filter(filteredMktDF("Date").lt(ym_end+1))
            }
        }

        DFArgs(filteredYMDF)
    }

}
