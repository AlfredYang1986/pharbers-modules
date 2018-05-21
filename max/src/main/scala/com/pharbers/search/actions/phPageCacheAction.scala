package com.pharbers.search.actions

import com.pharbers.sercuity.Sercurity
import com.pharbers.pactions.actionbase.{ListArgs, _}
import com.pharbers.driver.PhRedisDriver
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
        val result_df = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get

        val totalCount = result_df.count().toDouble
        val totalPage = Math.ceil(totalCount / pageSize).toInt
        redisDriver.addMap(pageCacheInfo, "count", totalCount)
        redisDriver.addMap(pageCacheInfo, "page", totalPage)

        if (totalCount != 0){
            val cacheIndex = pageIndex match {
                case i: Int if i < 2 => 0 to (i + 4)
                case i: Int if i > (totalPage - 2) => (totalPage - 4) to totalPage
                case i: Int => (i - 2) to (i + 2)
                case _ => ???
            }

            val result_rdd_limited = result_df.limit((cacheIndex.max + 1) * pageSize).rdd

            var phIndex = -1
            val initIndexRdd = result_rdd_limited.map { x =>
                phIndex += 1
                (phIndex, x)
            }

            val phIndexRdd = IndexedRDD(initIndexRdd)

            cacheIndex foreach { i =>
                val pageCacheTempKey = Sercurity.md5Hash(user + company + ym_condition + mkt + i + pageSize)
                val resultLst = ((i * pageSize) until (i * pageSize + pageSize)).map { x =>
                    phIndexRdd.get(x).get.toString()
                }.toList
                if(!redisDriver.exsits(pageCacheTempKey)){
                    redisDriver.addListRight(pageCacheTempKey, resultLst: _*)
                    redisDriver.expire(pageCacheTempKey, 5 * 60)
                }

            }

            NULLArgs
        } else {
            ListArgs(List.empty)
        }
    }
}
