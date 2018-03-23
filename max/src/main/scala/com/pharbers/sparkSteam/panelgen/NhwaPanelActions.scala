package com.pharbers.sparkSteam.panelgen

import com.pharbers.panel.format.input.nhwa.PhExcelNhwaFormat
import com.pharbers.sparkSteam.paction.actionbase.pActionTrait
import com.pharbers.sparkSteam.paction.{excelReadingTrait, jarPreloadTrait, panelGenerateTrait}
import com.pharbers.sparkSteam.panelgen.actionContainer.pActionContainer

trait NhwaPanelActions extends pActionContainer {
    override val actions: List[pActionTrait] =
        jarPreloadTrait() ::
            excelReadingTrait[PhExcelNhwaFormat]("resource/test-01.xlsx") ::
            panelGenerateTrait("") ::
            Nil
}