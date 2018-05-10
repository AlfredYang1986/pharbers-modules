package com.pharbers.search

import java.util.UUID

import com.mongodb.spark.rdd.MongoRDD
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.catalyst.plans.logical.MapPartitions
import org.bson.Document

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

        val pageIndex = 0
        val pageCount = 20
        val pageStartIndex = pageIndex*pageCount
        val pageEndIndex = pageStartIndex + pageCount - 1

        var partitionStartIndex = 0

        val sparkDriver = phSparkDriver()

        val redisDriver = new PhRedisDriver()
        val company = redisDriver.getMapValue(uid, "company")
        val singleJobKey = Sercurity.md5Hash(s"$company$ym$mkt")
        val max_count = redisDriver.getMapValue(singleJobKey, "max_count").toInt
        val max_path = redisDriver.getMapValue(singleJobKey, "max_path")
        val max_df = sparkDriver.csv2RDD(max_path, 31.toChar.toString)

        val max_rdd = max_df.rdd

        println(s"max_rdd.partitions = ${max_rdd.getNumPartitions}")
        println(s"max_rdd.count = ${max_count}")
        val max_repartitioned = max_rdd.repartition(max_count/pageCount)
        println(s"max_repartitioned.partitions = ${max_repartitioned.getNumPartitions}")

//        val test = max_repartitioned.mapPartitionsWithIndex{
//            (x,y) => {
//                val iter = List[String]().::(x + "|" + partitionStartIndex).iterator
//                partitionStartIndex += y.length
//                iter
//            }
//        }
//
//        test.coalesce(1).saveAsTextFile(s"/run/media/jeorch/82d99361-1346-4602-ae90-7a0a54c16e3f/jeorch/jeorch/test/max_test/_20180427/${UUID.randomUUID().toString}")

        val selectData = max_repartitioned.mapPartitionsWithIndex( (index,partition) => {
            val partitionStartIndex_temp = partitionStartIndex
            val partitionEndIndex = partitionStartIndex_temp + partition.length - 1
            partitionStartIndex = partitionEndIndex + 1
            if (partitionStartIndex_temp <= pageStartIndex && partitionEndIndex >= pageStartIndex){
                partition
            } else if (partitionStartIndex_temp <= pageEndIndex && partitionEndIndex >= pageEndIndex) {
                partition
            } else Iterator.empty
        })

//        val selectData = max_repartitioned.mapPartitionsWithIndex(
//            (index, page) =>{
//                if (index == pageIndex) page else Iterator.empty
//            }
//        )


        RDDArgs(selectData)
//        RDDArgs(test)
    }
}
