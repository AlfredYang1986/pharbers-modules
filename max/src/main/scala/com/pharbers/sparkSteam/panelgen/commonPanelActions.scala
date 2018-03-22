package com.pharbers.panelgen.actionContainer

import com.pharbers.sparkSteam.paction.actionbase.pActionTrait
import com.pharbers.sparkSteam.paction.{excelReadingTrait, jarPreloadTrait}
import com.pharbers.sparkSteam.panelgen.actionContainer.pActionContainer

trait commonPanelActions extends pActionContainer {
    override val actions: List[pActionTrait] = jarPreloadTrait() :: excelReadingTrait("resource/test-01.xlsx") :: Nil
}
