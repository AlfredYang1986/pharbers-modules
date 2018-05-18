package com.pharbers.search.actions

import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity
import edu.berkeley.cs.amplab.spark.indexedrdd.IndexedRDD
import edu.berkeley.cs.amplab.spark.indexedrdd.IndexedRDD._

/**
  * Created by jeorch on 18-5-9.
  */
object phPageSearchAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phPageSearchAction(args)
}

class phPageSearchAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "page_search_action"

    override def perform(pr: pActionArgs): pActionArgs = {
        val cachedPageData = pr.asInstanceOf[MapArgs].get("page_cache_action").asInstanceOf[ListArgs].get
        cachedPageData match {
            case Nil =>
                val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
                val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
                val ym_condition = defaultArgs.asInstanceOf[MapArgs].get("ym_condition").asInstanceOf[StringArgs].get
                val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
                val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
                val pageSize = defaultArgs.asInstanceOf[MapArgs].get("ps").asInstanceOf[StringArgs].get.toInt

                val pageStartIndex = pageIndex*pageSize
                val pageEndIndex = pageStartIndex + pageSize

                val redisDriver = new PhRedisDriver()
                val result_df = pr.asInstanceOf[MapArgs].get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get
                val result_rdd_limited = result_df.limit(pageEndIndex).rdd

                val (startTime, endTime) = ym_condition match {
                    case "" => ("", "")
                    case "-" => ("", "")
                    case _ => (ym_condition.split("-")(0), ym_condition.split("-")(1))
                }

                val singleSearchKey = Sercurity.md5Hash(user + company + startTime + endTime + mkt + pageSize)
                val totalIndex = redisDriver.getString(singleSearchKey) match {
                    case null => result_df.count().toInt - 1
                    case count => count.toInt - 1
                }
                if (result_rdd_limited.isEmpty()) ListArgs(List.empty)
                else {
                    var phIndex = -1
                    val initIndexRdd = result_rdd_limited.map(x => {
                        phIndex += 1
                        (phIndex, x)
                    })
                    val phIndexRdd = IndexedRDD(initIndexRdd)
                    val resultLst = (pageStartIndex until pageEndIndex).map(x => {
                        if(x > totalIndex) StringArgs(null) else StringArgs(phIndexRdd.get(x).get.toString())
                    }).toList.filter(_!=StringArgs(null))

                    ListArgs(resultLst)
                }
            case _ => ListArgs(cachedPageData)
        }
    }
}
