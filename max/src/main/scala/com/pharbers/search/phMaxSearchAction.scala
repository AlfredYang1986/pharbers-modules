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

        val pageIndex = 2
        val pageCount = 20
        val pageStartIndex = pageIndex*pageCount
        val pageEndIndex = pageStartIndex + pageCount - 1
        val delimiter = 31.toChar.toString

        var partitionStartIndex = 0

        val sparkDriver = phSparkDriver()

        val redisDriver = new PhRedisDriver()
        val company = redisDriver.getMapValue(uid, "company")
        val singleJobKey = Sercurity.md5Hash(s"$company$ym$mkt")
        val max_count = redisDriver.getMapValue(singleJobKey, "max_count").toInt
        val max_path = redisDriver.getMapValue(singleJobKey, "max_path")
        val max_df = sparkDriver.csv2RDD(max_path, 31.toChar.toString)

//        val max_repartitioned = max_df.rdd.repartition(max_count/pageCount)
//        val max_repartitioned = max_df.rdd.repartition(1)

//        val test = max_repartitioned.mapPartitionsWithIndex{
//            (x,y) => {
//                val l = y.length
//                val iter = List[String]().::(x + delimiter + l + delimiter + partitionStartIndex).iterator
//                partitionStartIndex += l
//                iter
//            }
//        }
//
//        val testUUID = UUID.randomUUID().toString
//        println("CUN")
//        test.saveAsTextFile(s"/run/media/jeorch/82d99361-1346-4602-ae90-7a0a54c16e3f/jeorch/jeorch/test/max_test/_20180427/${testUUID}")
//        println("CUN##")
//        val cacheIndexData = sparkDriver.sc.textFile(s"/run/media/jeorch/82d99361-1346-4602-ae90-7a0a54c16e3f/jeorch/jeorch/test/max_test/_20180427/${testUUID}")
//        val testIndexCache = cacheIndexData.map(x => {
//            val splitArray = x.split(delimiter)
//            val partitionStartIndex_temp = splitArray(2).toInt
//            val partitionEndIndex = partitionStartIndex_temp + splitArray(1).toInt
//            if (partitionStartIndex_temp <= pageStartIndex && partitionStartIndex_temp <= pageStartIndex && partitionEndIndex >= pageStartIndex) splitArray(0).toInt else 0
//        })
//
//
//
////        val selectData = max_repartitioned.mapPartitionsWithIndex( (index,partition) => {
////            val partitionStartIndex_temp = partitionStartIndex
////            val partitionEndIndex = partitionStartIndex_temp + partition.length - 1
////            partitionStartIndex = partitionEndIndex + 1
////            if (partitionStartIndex_temp <= pageStartIndex && partitionEndIndex >= pageStartIndex){
////                partition
////            } else if (partitionStartIndex_temp <= pageEndIndex && partitionEndIndex >= pageEndIndex) {
////                partition
////            } else Iterator.empty
////        })
//
//        val selectIndex = testIndexCache.sum().toInt
//        val selectData = max_repartitioned.mapPartitionsWithIndex(
//            (index, page) =>{
//                if (index == selectIndex) page else if(index == selectIndex+1) page else Iterator.empty
//            }
//        )
//
////        val selectData = max_repartitioned.mapPartitionsWithIndex(
////            (index, page) =>{
////                if (index == pageIndex) page else Iterator.empty
////            }
////        )
//
//        ListArgs(selectData.take(20).toList.asInstanceOf[List[pActionArgs]])

//        val rdd = sparkDriver.sc.parallelize((1 to 1000000).map(x => (x.toLong, 0)))
//        val indexed = IndexedRDD(rdd).cache()
//        val phIndexRdd = indexed.put(20L, 2018).cache()

        val max_rdd = max_df.rdd

        var phIndex = 0
        val initIndexRdd = max_rdd.map(x => {
            phIndex +=1
            (phIndex, x)
        })
        val phIndexRdd = IndexedRDD(initIndexRdd)/*.cache()*/


        val resultLst = (pageStartIndex to pageEndIndex).map(x => {
            StringArgs(phIndexRdd.get(x).get.toString())
        }).toList

        ListArgs(resultLst)
    }
}
