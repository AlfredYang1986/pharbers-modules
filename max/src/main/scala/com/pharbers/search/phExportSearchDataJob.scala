package com.pharbers.search

import com.pharbers.search.actions._
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{jarPreloadAction, setLogLevelAction}

/**
  * Created by jeorch on 18-6-4.
  */
object phExportSearchDataJob {
    def apply(args: Map[String, String]) : phExportSearchDataJob = {
        new phExportSearchDataJob {
            override lazy val company: String = args.getOrElse("company", throw new Exception("Illegal company"))
            override lazy val ym_condition: String = args.getOrElse("ym_condition", "-")
            override lazy val mkt: String = args.getOrElse("mkt", "")
        }
    }
}

trait phExportSearchDataJob extends sequenceJobWithMap {
    override val name: String = "phExportSearchDataJob"

    val company: String
    val ym_condition: String
    val mkt: String

    lazy val searchArgs = MapArgs(
        Map(
            "company" -> StringArgs(company),
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

