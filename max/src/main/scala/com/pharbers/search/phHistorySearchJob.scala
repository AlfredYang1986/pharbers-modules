package com.pharbers.search

import com.pharbers.search.actions._
import com.pharbers.pactions.jobs.{NULLJob, choice3pJob, sequenceJobWithMap}
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{jarPreloadAction, setLogLevelAction}

/**
  * Created by jeorch on 18-5-11.
  */
object phHistorySearchJob {
    def apply(args: Map[String, String]) : phHistorySearchJob = {
        new phHistorySearchJob {
            override lazy val user: String = args.getOrElse("user", throw new Exception("Illegal user"))
            override lazy val company: String = args.getOrElse("company", throw new Exception("Illegal company"))
            override lazy val pageIndex: String = args.getOrElse("pageIndex", "0")
            override lazy val singlePageSize: String = args.getOrElse("singlePageSize", "20")
            override lazy val ym_condition: String = args.getOrElse("ym_condition", "-")
            override lazy val mkt: String = args.getOrElse("mkt", "")
        }
    }
}

trait phHistorySearchJob extends sequenceJobWithMap {
    override val name: String = "phHistorySearchJob"

    val user: String
    val company: String
    val pageIndex: String
    val singlePageSize: String
    val ym_condition: String
    val mkt: String

    lazy val searchArgs = MapArgs(
        Map(
            "user" -> StringArgs(user),
            "company" -> StringArgs(company),
            "pi" -> StringArgs(pageIndex),
            "ps" -> StringArgs(singlePageSize),
            "ym_condition" -> StringArgs(ym_condition),
            "mkt" -> StringArgs(mkt)
        )
    )

    val checkPageCache: choice3pJob = new choice3pJob {
        override val name = "checkPageCache"
        val actions: List[pActionTrait] = phCheckPageCacheAction(searchArgs) ::
            NULLJob ::
            new sequenceJobWithMap {
                override val name: String = "cache_data_job"
                override val actions: List[pActionTrait] =
                    phHistoryConditionSearchAction(searchArgs) ::
                        phReadHistoryResultAction(searchArgs) ::
                        phPageCacheAction(searchArgs) ::
                        Nil

            } :: Nil
    }

    override val actions: List[pActionTrait] = jarPreloadAction() ::
            setLogLevelAction("ERROR") ::
            checkPageCache ::
            phReturnPageCacheAction(searchArgs) ::
            Nil
}
