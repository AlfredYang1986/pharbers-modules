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

class phPageCacheAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "page_cache_action"

    override def perform(pr: pActionArgs): pActionArgs = {
        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val ym_condition = defaultArgs.asInstanceOf[MapArgs].get("ym_condition").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
        val pageSize = defaultArgs.asInstanceOf[MapArgs].get("ps").asInstanceOf[StringArgs].get.toInt
        val redisDriver = new PhRedisDriver()

        val pageCacheInfo = Sercurity.md5Hash(user + company + ym_condition + mkt)
        val result_rdd = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get.rdd

        if (result_rdd.isEmpty()) ListArgs(List.empty)
        else {
            val totalCount = result_rdd.count().toDouble
            val totalPage = Math.ceil(totalCount / pageSize).toInt
            redisDriver.addMap(pageCacheInfo, "count", totalCount)
            redisDriver.addMap(pageCacheInfo, "page", totalPage)

            val result_rdd_limited = result_rdd.zipWithIndex
                    .filter(_._2 < pageSize * totalPage)
                    .map(x => x._2.toInt -> x._1)

            val phIndexRdd = IndexedRDD(result_rdd_limited)

            val cacheIndex = pageIndex match {
                case i: Int if i < 2 => 1 to (i + 4)
                case i: Int if i > (totalPage - 2) => (totalPage - 4) to totalPage
                case i: Int => (i - 2) to (i + 2)
                case _ => ???
            }

            cacheIndex map { x =>
                val pageCacheTempKey = Sercurity.md5Hash(user + company + ym_condition + mkt + x + pageSize)
                val resultLst = ((x * pageSize) until (x * pageSize + pageSize)).map(x =>
                    phIndexRdd.get(x).get.toString()
                ).toList
                redisDriver.addListRight(pageCacheTempKey, resultLst: _*)
                x
            }

            NULLArgs
        }
    }
}
