package com.pharbers.panelgen.actionContainer

import com.pharbers.panel.format.input.nhwa.PhExcelNhwaFormat
import com.pharbers.sparkSteam.paction.actionbase.pActionTrait
import com.pharbers.sparkSteam.paction.{calcYMTrait, excelReadingTrait, jarPreloadTrait}
import com.pharbers.sparkSteam.panelgen.actionContainer.pActionContainer

trait NhwaYMActions extends pActionContainer {
    override val actions: List[pActionTrait] =
        jarPreloadTrait() ::
            excelReadingTrait[PhExcelNhwaFormat]("resource/test-01.xlsx") ::
            calcYMTrait("") ::
            Nil
}
