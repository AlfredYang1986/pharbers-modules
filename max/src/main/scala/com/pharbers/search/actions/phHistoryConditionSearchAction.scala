package com.pharbers.search.actions

import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity

/**
  * Created by jeorch on 18-5-14.
  */
object phHistoryConditionSearchAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phHistoryConditionSearchAction(args)
}

class phHistoryConditionSearchAction(override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = "phHistoryConditionSearchAction"

    override def perform(pr: pActionArgs): pActionArgs = {
        val redisDriver = new PhRedisDriver()

        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val ym_condition = defaultArgs.asInstanceOf[MapArgs].get("ym_condition").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get

        val userJobsKey = Sercurity.md5Hash(user + company)

        val allSingleJobKeyLst = redisDriver.getSetAllValue(userJobsKey).map(singleJobKey =>
            (
                    singleJobKey,
                    redisDriver.getMapValue(singleJobKey, "ym"),
                    redisDriver.getMapValue(singleJobKey, "mkt")
            )
        ).toList

        val filteredYMKeyLst = ym_condition match {
            case "" => allSingleJobKeyLst
            case "-" => allSingleJobKeyLst
            case _ =>
                val ym_start = ym_condition.split("-")(0).toInt
                val ym_end = ym_condition.split("-")(1).toInt
                allSingleJobKeyLst.filter(x => x._2.toInt>=ym_start).filter(x => x._2.toInt<=ym_end)
        }

        val filteredMktKeyLst = mkt match {
            case "" => filteredYMKeyLst
            case "All" => filteredYMKeyLst
            case _ => filteredYMKeyLst.filter(x => x._3 == mkt)
        }

        ListArgs(filteredMktKeyLst.map(x => StringArgs(x._1)))
    }
}