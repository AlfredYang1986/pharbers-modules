package com.pharbers.panelgen.actionContainer
import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.paction.excelReadingTrait

trait commonPanelActions extends pActionContainer {
    override val actions: List[pActionTrait] = excelReadingTrait("") :: Nil
}
