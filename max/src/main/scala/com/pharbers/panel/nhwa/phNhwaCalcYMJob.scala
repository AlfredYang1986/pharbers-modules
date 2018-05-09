package com.pharbers.panel.nhwa

import java.util.UUID

import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.jobs.sequenceJob
import com.pharbers.panel.common.phCalcYM2JVJob
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat
import com.pharbers.panel.panel_path_obj
import org.apache.spark.addListenerAction

object phNhwaCalcYMJob {

    def apply(cpa_path : String) : phNhwaCalcYMJob = {
        new phNhwaCalcYMJob {
            override lazy val cpa_file: String = cpa_path
            override lazy val cache_location: String = panel_path_obj.p_cachePath + UUID.randomUUID().toString
        }
    }
}

trait phNhwaCalcYMJob extends sequenceJob {
    override val name: String = "phNhwaCalcYMJob"
    val cpa_file: String
    val cache_location: String

    override val actions: List[pActionTrait] = { jarPreloadAction() ::
                setLogLevelAction("ERROR") ::
                xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                addListenerAction(MaxSparkListener("testUser", "ymCalc")(0, 100)) ::
                phNhwaCalcYMConcretJob() ::
                saveCurrenResultAction(cache_location) ::
                phCalcYM2JVJob() ::
                Nil
}
