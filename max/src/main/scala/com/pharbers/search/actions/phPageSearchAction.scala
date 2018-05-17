package com.pharbers.search.actions

import com.pharbers.pactions.actionbase._
import edu.berkeley.cs.amplab.spark.indexedrdd.IndexedRDD
import edu.berkeley.cs.amplab.spark.indexedrdd.IndexedRDD._

/**
  * Created by jeorch on 18-5-9.
  */
object phPageSearchAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phPageSearchAction(args)
}

class phPageSearchAction(override val defaultArgs: pActionArgs) extends pActionTrait with java.io.Serializable {
    override val name: String = "page_search_action"

    override def perform(pr: pActionArgs): pActionArgs = {
        val cachedPageData = pr.asInstanceOf[MapArgs].get("page_cache_action").asInstanceOf[ListArgs].get
        cachedPageData match {
            case Nil =>
                val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
                val pageSize = defaultArgs.asInstanceOf[MapArgs].get("ps").asInstanceOf[StringArgs].get.toInt
                val pageStartIndex = pageIndex*pageSize

                val pageEndIndex = pageStartIndex + pageSize
                val result_df = pr.asInstanceOf[MapArgs].get("phHistoryConditionSearchAction").asInstanceOf[DFArgs].get
                val result_rdd_limited = result_df.limit(pageEndIndex).rdd

                if (result_rdd_limited.isEmpty()) ListArgs(List.empty)
                else {
                    var phIndex = -1
                    val initIndexRdd = result_rdd_limited.map(x => {
                        phIndex += 1
                        (phIndex, x)
                    })
                    val phIndexRdd = IndexedRDD(initIndexRdd)
                    val resultLst = (pageStartIndex until pageEndIndex).map(x => {
                        StringArgs(phIndexRdd.get(x).get.toString())
                    }).toList

                    ListArgs(resultLst)
                }
            case _ => ListArgs(cachedPageData)
        }
    }
}
