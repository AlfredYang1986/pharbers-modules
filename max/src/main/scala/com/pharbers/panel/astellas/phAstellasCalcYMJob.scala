package com.pharbers.panel.astellas

import java.util.UUID
import com.pharbers.panel.common._
import com.pharbers.panel.panel_path_obj
import com.pharbers.panel.astellas.format._
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.jobs.sequenceJobWithMap

object phAstellasCalcYMJob {

    def apply(cpa_path: String, gyc_path: String) : phAstellasCalcYMJob = {
        new phAstellasCalcYMJob {
            override lazy val cpa_file: String = cpa_path
            override lazy val gyc_file: String = gyc_path
            override lazy val cache_location: String = panel_path_obj.p_cachePath + UUID.randomUUID().toString + "/"
        }
    }
}

trait phAstellasCalcYMJob extends sequenceJobWithMap {
    override val name: String = "phNhwaCalcYMJob"
    val cpa_file: String
    val gyc_file: String
    val cache_location: String

    override val actions: List[pActionTrait] = jarPreloadAction() ::
                xlsxReadingAction[phAstellasCpaFormat](cpa_file, "cpa") ::
                xlsxReadingAction[phAstellasGycxFormat](gyc_file, "gycx") ::
                phAstellasCalcYMCpaConcretJob() ::
                phAstellasCalcYMGycxConcretJob() ::
                saveMapResultAction("calcYMWithCpa", cache_location + "cpa") ::
                saveMapResultAction("calcYMWithGycx", cache_location + "gycx") ::
                phCalcYM2JVJobWithCpaAndGyc() ::
                Nil
} 