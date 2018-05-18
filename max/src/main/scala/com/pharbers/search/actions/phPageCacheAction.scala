package com.pharbers.search.actions

import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity
import edu.berkeley.cs.amplab.spark.indexedrdd.IndexedRDD
import edu.berkeley.cs.amplab.spark.indexedrdd.IndexedRDD._

/**
  * Created by jeorch on 18-5-11.
  */
object phPageCacheAction{
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phPageCacheAction(args)
}

class phPageCacheAction(override val defaultArgs: pActionArgs) extends pActionTrait with java.io.Serializable {
    override val name: String = "page_cache_action"

    private val defaultCachePageCount: Int = 5

    override def perform(pr: pActionArgs): pActionArgs = {
        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val ym_condition = defaultArgs.asInstanceOf[MapArgs].get("ym_condition").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
        val pageSize = defaultArgs.asInstanceOf[MapArgs].get("ps").asInstanceOf[StringArgs].get.toInt
        val itemStartIndex = pageIndex*pageSize
        val itemEndIndex = itemStartIndex + pageSize
        val redisDriver = new PhRedisDriver()
        val pageCacheKey = Sercurity.md5Hash(user + company + ym_condition + mkt + pageIndex + pageSize)
        val cachedPageData = redisDriver.getListAllValue(pageCacheKey)

        cachedPageData match {
            case Nil =>
                val result_df = pr.asInstanceOf[MapArgs].get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get
                if (result_df.rdd.isEmpty()) ListArgs(List.empty)
                else {
                    val result_rdd_limited = result_df.limit(itemEndIndex).rdd
                    val (startTime, endTime) = ym_condition match {
                        case "" => ("", "")
                        case "-" => ("", "")
                        case _ => (ym_condition.split("-")(0), ym_condition.split("-")(1))
                    }
                    val singleSearchKey = Sercurity.md5Hash(user + company + startTime + endTime + mkt + pageSize)
                    val totalItemIndex = redisDriver.getString(singleSearchKey) match {
                        case null => result_df.count().toInt - 1
                        case count => count.toInt - 1
                    }
                    var phIndex = -1
                    val initIndexRdd = result_rdd_limited.map(x => {
                        phIndex += 1
                        (phIndex, x)
                    })
                    val phIndexRdd = IndexedRDD(initIndexRdd)

                    val (cacheStartPage, cacheEndPage) = pageIndex match {
                        case i if (i < 5) => (0, 5)
                        case i if (i > (totalItemIndex/pageSize)) => (totalItemIndex/pageSize - 2, totalItemIndex/pageSize + 2)
                        case i => (i - 2, i + 2)
                    }

                    cacheStartPage until cacheEndPage foreach(index =>{
                        val pageCacheTempKey = Sercurity.md5Hash(user + company + ym_condition + mkt + index + pageSize)
                        val resultLst = (itemStartIndex until itemEndIndex).map(x => {
                            if(x > totalItemIndex) StringArgs(null) else StringArgs(phIndexRdd.get(x).get.toString())
                        }).toList.filter(_!=StringArgs(null))
                        if (!redisDriver.exsits(pageCacheTempKey)) {
                            redisDriver.addListRight(pageCacheTempKey, resultLst:_*)
                            redisDriver.expire(pageCacheTempKey, 10*60)
                        }
                    })
                    ListArgs(redisDriver.getListAllValue(pageCacheKey).map(StringArgs))
                }

            case _ => ListArgs(cachedPageData.map(StringArgs))
        }
    }
}
