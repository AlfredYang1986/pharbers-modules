package com.pharbers.search

import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{jarPreloadAction, setLogLevelAction}
import com.pharbers.pactions.jobs.{NULLJob, choice3pJob, sequenceJobWithMap}
import com.pharbers.search.actions._

/**
  * Created by jeorch on 18-6-4.
  */
object phExportSearchDataJob {
    def apply(args: Map[String, String]) : phExportSearchDataJob = {
        new phExportSearchDataJob {
            override lazy val user: String = args.get("user").getOrElse(throw new Exception("Illegal user"))
            override lazy val company: String = args.get("company").getOrElse(throw new Exception("Illegal company"))
            override lazy val pageIndex: String = args.get("pageIndex").getOrElse("0")
            override lazy val singlePageSize: String = args.get("singlePageSize").getOrElse("20")
            override lazy val ym_condition: String = args.get("ym_condition").getOrElse("-")
            override lazy val mkt: String = args.get("mkt").getOrElse("")
        }
    }
}

trait phExportSearchDataJob extends sequenceJobWithMap {
    override val name: String = "phExportSearchDataJob"

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

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        setLogLevelAction("ERROR") ::
        phHistoryConditionSearchAction(searchArgs) ::
        phReadHistoryResultAction(searchArgs) ::
        phExportSearchDataAction(searchArgs) ::
        Nil
}

