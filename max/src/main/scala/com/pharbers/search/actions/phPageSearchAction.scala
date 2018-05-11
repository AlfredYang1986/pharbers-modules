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
        val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
        val pageCount = defaultArgs.asInstanceOf[MapArgs].get("pc").asInstanceOf[StringArgs].get.toInt

        val pageStartIndex = pageIndex*pageCount
        val pageEndIndex = pageStartIndex + pageCount - 1

        val result_df = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get

        val result_rdd_limited = result_df.limit(pageEndIndex + 1).rdd

        var phIndex = -1
        val initIndexRdd = result_rdd_limited.map(x => {
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
