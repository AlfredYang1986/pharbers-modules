package com.pharbers.search

import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{jarPreloadAction, setLogLevelAction}
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.search.actions.{phPageCacheAction, phPageSearchAction, phReadHistoryResultAction}

/**
  * Created by jeorch on 18-5-11.
  */
object phHistorySearchJob {
    def apply(uid_arg: String, page_index_arg: Int, page_size_arg: Int) : phHistorySearchJob = {
        new phHistorySearchJob {
            override lazy val uid: String = uid_arg
            override lazy val pageIndex: Int = page_index_arg
            override lazy val singlePageSize: Int = page_size_arg
        }
    }
}

trait phHistorySearchJob extends sequenceJobWithMap {
    override val name: String = "phHistorySearchJob"

    val uid: String
    val pageIndex: Int
    val singlePageSize: Int

    val searchArgs = MapArgs(
        Map(
            "uid" -> StringArgs(uid),
            "search_type" -> StringArgs(name),
            "pi" -> StringArgs(pageIndex.toString),
            "ps" -> StringArgs(singlePageSize.toString)
        )
    )

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        setLogLevelAction("ERROR") ::
        phReadHistoryResultAction(searchArgs) ::
        phPageCacheAction(searchArgs) ::
        phPageSearchAction(searchArgs) ::
        Nil
}
