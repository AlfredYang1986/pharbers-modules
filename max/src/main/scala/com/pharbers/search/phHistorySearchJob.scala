package com.pharbers.search

import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{jarPreloadAction, setLogLevelAction}
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.search.actions.{phPageSearchAction, phReadHistoryResultAction}

/**
  * Created by jeorch on 18-5-11.
  */
object phHistorySearchJob {
    def apply(uid_arg: String, page_index_arg: Int, page_count_arg: Int) : phHistorySearchJob = {
        new phHistorySearchJob {
            override lazy val uid: String = uid_arg
            override lazy val pageIndex: Int = page_index_arg
            override lazy val singlePageCount: Int = page_count_arg
        }
    }
}

trait phHistorySearchJob extends sequenceJobWithMap {
    override val name: String = "phHistorySearchJob"

    val uid: String
    val pageIndex: Int
    val singlePageCount: Int

    val searchArgs = MapArgs(
        Map(
            "uid" -> StringArgs(uid),
            "pi" -> StringArgs(pageIndex.toString),
            "pc" -> StringArgs(singlePageCount.toString)
        )
    )

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        setLogLevelAction("ERROR") ::
        phReadHistoryResultAction(searchArgs) ::
        phPageSearchAction(searchArgs) ::
        Nil
}
