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
        val uid = defaultArgs.asInstanceOf[MapArgs].get("uid").asInstanceOf[StringArgs].get
        val search_type = defaultArgs.asInstanceOf[MapArgs].get("search_type").asInstanceOf[StringArgs].get
        val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
        val pageSize = defaultArgs.asInstanceOf[MapArgs].get("ps").asInstanceOf[StringArgs].get.toInt
        val redisDriver = new PhRedisDriver()
        val company = redisDriver.getMapValue(uid, "company")
        val pageCacheKey = Sercurity.md5Hash(s"$uid$company$search_type$pageIndex$pageSize")
        val cachedPageData = redisDriver.getListAllValue(pageCacheKey)

        cachedPageData match {
            case Nil => {
                val result_df = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get
                val result_rdd_limited = result_df.limit(pageSize*defaultCachePageCount).rdd
                var phIndex = -1
                val initIndexRdd = result_rdd_limited.map(x => {
                    phIndex += 1
                    (phIndex, x)
                })
                val phIndexRdd = IndexedRDD(initIndexRdd)
                0 to defaultCachePageCount-1 foreach(index =>{

                    val pageCacheTempKey = Sercurity.md5Hash(s"$uid$company$search_type$index$pageSize")

                    val resultLst = (index*pageSize to index*pageSize+pageSize-1).map(x => {
                        StringArgs(phIndexRdd.get(x).get.toString())
                    }).toList
                    redisDriver.addListRight(pageCacheTempKey,resultLst:_*)
                })
                ListArgs(redisDriver.getListAllValue(pageCacheKey).map(StringArgs(_)))
            }
            case _ => ListArgs(cachedPageData.map(StringArgs(_)))
        }
    }
}
