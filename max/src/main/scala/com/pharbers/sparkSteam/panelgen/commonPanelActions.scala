package com.pharbers.sparkSteam.panelgen.actionContainer
import com.pharbers.sparkSteam.paction.actionbase.pActionTrait
import com.pharbers.sparkSteam.paction.excelReadingTrait

trait commonPanelActions extends pActionContainer {
    override val actions: List[pActionTrait] = excelReadingTrait("resource/test-01.xlsx") :: Nil
}
