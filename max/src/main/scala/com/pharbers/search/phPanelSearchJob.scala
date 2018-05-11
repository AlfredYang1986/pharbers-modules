package com.pharbers.search

import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{jarPreloadAction, setLogLevelAction}
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.search.actions.{phPageSearchAction, phReadPanelResultAction}

/**
  * Created by jeorch on 18-5-11.
  */
object phPanelSearchJob {
    def apply(uid_arg: String, ym_arg: String, mkt_arg: String, page_index_arg: Int, page_count_arg: Int) : phPanelSearchJob = {
        new phPanelSearchJob {
            override lazy val uid: String = uid_arg
            override lazy val ym: String = ym_arg
            override lazy val mkt: String = mkt_arg
            override lazy val pageIndex: Int = page_index_arg
            override lazy val singlePageCount: Int = page_count_arg
        }
    }
}

trait phPanelSearchJob extends sequenceJobWithMap {
    override val name: String = "phPanelSearchJob"

    val uid: String
    val ym: String
    val mkt: String
    val pageIndex: Int
    val singlePageCount: Int

    val searchArgs = MapArgs(
        Map(
            "uid" -> StringArgs(uid),
            "ym" -> StringArgs(ym),
            "mkt" -> StringArgs(mkt),
            "pi" -> StringArgs(pageIndex.toString),
            "pc" -> StringArgs(singlePageCount.toString)
        )
    )

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        setLogLevelAction("ERROR") ::
        phReadPanelResultAction(searchArgs) ::
        phPageSearchAction(searchArgs) ::
        Nil
}
