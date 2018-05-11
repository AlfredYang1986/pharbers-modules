package com.pharbers.search

import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver
import edu.berkeley.cs.amplab.spark.indexedrdd.IndexedRDD
import edu.berkeley.cs.amplab.spark.indexedrdd.IndexedRDD._

/**
  * Created by jeorch on 18-5-9.
  */
object phMaxSearchAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxSearchAction(args)
}

class phMaxSearchAction(override val defaultArgs: pActionArgs) extends pActionTrait with java.io.Serializable {
    override val name: String = "max_search_action"

    override def perform(pr: pActionArgs): pActionArgs = {
        val uid = defaultArgs.asInstanceOf[MapArgs].get("uid").asInstanceOf[StringArgs].get
        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
        val pageCount = defaultArgs.asInstanceOf[MapArgs].get("pc").asInstanceOf[StringArgs].get.toInt

        val pageStartIndex = pageIndex*pageCount
        val pageEndIndex = pageStartIndex + pageCount - 1

        val sparkDriver = phSparkDriver()

        val redisDriver = new PhRedisDriver()
        val company = redisDriver.getMapValue(uid, "company")
        val singleJobKey = Sercurity.md5Hash(s"$company$ym$mkt")
        val max_path = redisDriver.getMapValue(singleJobKey, "max_path")
        val max_df = sparkDriver.csv2RDD(max_path, 31.toChar.toString)

        val max_rdd_limited = max_df.limit(pageEndIndex + 1).rdd

        var phIndex = -1
        val initIndexRdd = max_rdd_limited.map(x => {
            phIndex += 1
            (phIndex, x)
        })
        val phIndexRdd = IndexedRDD(initIndexRdd)


        val resultLst = (pageStartIndex to pageEndIndex).map(x => {
            StringArgs(phIndexRdd.get(x).get.toString())
        }).toList

        ListArgs(resultLst)
    }
}
