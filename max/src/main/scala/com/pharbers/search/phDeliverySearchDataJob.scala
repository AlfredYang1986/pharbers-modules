package com.pharbers.search

import com.pharbers.builder.phMarketTable.Builderimpl
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{jarPreloadAction, setLogLevelAction}
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.search.actions.{phHistoryConditionSearchAction, phReadHistoryResultAction}

/**
  * Created by jeorch on 18-6-5.
  */
object phDeliverySearchDataJob {
    def apply(args: Map[String, String]) : phDeliverySearchDataJob = {
        new phDeliverySearchDataJob {
            override lazy val company: String = args.getOrElse("company", throw new Exception("Illegal company"))
            override lazy val ym_condition: String = args.getOrElse("ym_condition", "-")
            override lazy val mkt: String = args.getOrElse("mkt", "")
        }
    }
}

trait phDeliverySearchDataJob  extends sequenceJobWithMap {
    override val name: String = "phDeliverySearchDataJob"

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

    val builderimpl = Builderimpl(company)
    import builderimpl._
    val deliveryInstMap: Map[String, String] = getDeliveryInst(mkt)

    val deliveryAction: pActionTrait = implWithoutActor(deliveryInstMap("instance"),
        Map("company" -> company, "ym_condition" -> ym_condition, "mkt" -> mkt) ++ deliveryInstMap)

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        setLogLevelAction("ERROR") ::
        phHistoryConditionSearchAction(searchArgs) ::
        phReadHistoryResultAction(searchArgs) ::
        deliveryAction ::
        Nil
}
