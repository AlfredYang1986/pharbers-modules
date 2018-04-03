package com.pharbers.panel.astellas

import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.pactions.generalactions.{jarPreloadAction, xlsxReadingAction}
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.panel.astellas.format.{phAstellasCpaFormat, phAstellasGycxFormat}

trait phAstellasCalcYMActionsTrait extends sequenceJobWithMap {
    val cpa_file: String
    val gycx_file: String

    override val actions: List[pActionTrait] = jarPreloadAction() ::
                xlsxReadingAction[phAstellasCpaFormat](cpa_file, "cpa") ::
                xlsxReadingAction[phAstellasGycxFormat](gycx_file, "gycx") ::
                phAstellasCalcYMImplAction("ymLst") ::
                Nil
} 