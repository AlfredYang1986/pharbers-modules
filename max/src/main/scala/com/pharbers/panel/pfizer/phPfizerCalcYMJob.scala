package com.pharbers.panel.pfizer

import java.util.UUID

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions.{jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.pactions.jobs.sequenceJob
import com.pharbers.panel.common.phCalcYM2JVJob
import com.pharbers.panel.pfizer.format.phPfizerCpaFormat

/**
  * Created by jeorch on 18-4-18.
  */
object phPfizerCalcYMJob {

    def apply(cpa_path : String) : phPfizerCalcYMJob = {
        new phPfizerCalcYMJob {
            override lazy val cpa_file: String = cpa_path
            override lazy val cache_location: String = max_path_obj.p_cachePath + UUID.randomUUID().toString
        }
    }
}

trait phPfizerCalcYMJob extends sequenceJob {
    override val name: String = "phPfizerCalcYMJob"
    val cpa_file: String
    val cache_location: String

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        xlsxReadingAction[phPfizerCpaFormat](cpa_file, "cpa") ::
        phPfizerCalcYMAction() ::
        saveCurrenResultAction(cache_location) ::
        phCalcYM2JVJob() ::
        Nil
}