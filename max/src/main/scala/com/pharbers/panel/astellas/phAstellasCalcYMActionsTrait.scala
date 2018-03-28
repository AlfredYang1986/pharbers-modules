package com.pharbers.panel.astellas

import com.pharbers.paction.actionContainer.pMapActionContainer
import com.pharbers.paction.funcTrait.{jarPreloadTrait, xlsxReadingTrait}
import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.panel.astellas.format.{phAstellasCpaFormat, phAstellasGycxFormat}

trait phAstellasCalcYMActionsTrait extends pMapActionContainer {
    val cpa_file: String
    val gycx_file: String

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
                xlsxReadingTrait[phAstellasCpaFormat](cpa_file, "cpa") ::
                xlsxReadingTrait[phAstellasGycxFormat](gycx_file, "gycx") ::
                phAstellasCalcYMImplAction("ymLst") ::
                Nil
} 