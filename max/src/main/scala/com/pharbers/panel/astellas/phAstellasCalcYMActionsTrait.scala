package com.pharbers.panel.astellas

import com.pharbers.paction.actionContainer.pMapActionContainer
import com.pharbers.paction.generalactions.{jarPreloadAction, xlsxReadingAction}
import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.panel.astellas.format.{phAstellasCpaFormat, phAstellasGycxFormat}

trait phAstellasCalcYMActionsTrait extends pMapActionContainer {
    val cpa_file: String
    val gycx_file: String

    override val actions: List[pActionTrait] = jarPreloadAction() ::
                xlsxReadingAction[phAstellasCpaFormat](cpa_file, "cpa") ::
                xlsxReadingAction[phAstellasGycxFormat](gycx_file, "gycx") ::
                phAstellasCalcYMImplAction("ymLst") ::
                Nil
} 