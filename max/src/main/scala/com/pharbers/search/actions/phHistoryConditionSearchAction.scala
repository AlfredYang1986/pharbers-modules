package com.pharbers.search.actions

import java.util.Base64

import com.pharbers.common.algorithm.phSparkCommonFuncTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
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

        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val ym_condition = defaultArgs.asInstanceOf[MapArgs].get("ym_condition").asInstanceOf[StringArgs].get
        val redisDriver = new PhRedisDriver()

        //TODO:临时解决大数据量最后一页的方案
        val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
        val pageSize = defaultArgs.asInstanceOf[MapArgs].get("ps").asInstanceOf[StringArgs].get.toInt
        val pageCacheInfo = Sercurity.md5Hash(user + company + ym_condition + mkt)
        val totalCount = redisDriver.getMapValue(pageCacheInfo, "count")

        val maxSingleDayJobsKey = Sercurity.md5Hash("Pharbers")
        val db = new dbInstanceManager{}.queryDBInstance("data").get
        val historyKeySet = db.getOneDBAllCollectionNames

        val totalSingleJobKeySet = redisDriver.getSetAllValue(maxSingleDayJobsKey) match {
            case s if(s.isEmpty) => historyKeySet.toSet
            case s => s.foreach(historyKeySet.add(_)); historyKeySet.toSet
        }

        val allSingleJobKeyLst = totalSingleJobKeySet
            .map(singleJobKey => {
                val singleJobInfoArr = new String(Base64.getDecoder.decode(singleJobKey)).split("#")
                (
                    singleJobInfoArr(0),
                    singleJobInfoArr(1),
                    singleJobInfoArr(2),
                    singleJobKey
                )
            })
            .filter(x => x._1 == company).toList

        val filteredYMKeyLst = ym_condition match {
            case "" => allSingleJobKeyLst
            case "-" => allSingleJobKeyLst
            case _ =>
                val ym_start = ym_condition.split("-")(0).toInt
                val ym_end = ym_condition.split("-")(1).toInt
                allSingleJobKeyLst.filter(x => x._2.toInt >= ym_start).filter(x => x._2.toInt <= ym_end)
        }

        val filteredMktKeyLst = mkt match {
            case "" => filteredYMKeyLst
            case "All" => filteredYMKeyLst
            case _ => filteredYMKeyLst.filter(x => x._3 == mkt)
        }

        //TODO:临时解决大数据量最后一页的方案
        if(totalCount != null && totalCount.toDouble != 0 && pageIndex == (totalCount.toDouble.toInt/pageSize)){
            ListArgs((filteredMktKeyLst.last::Nil).map(x => StringArgs(x._4)))
        } else ListArgs(filteredMktKeyLst.map(x => StringArgs(x._4)))
    }
}