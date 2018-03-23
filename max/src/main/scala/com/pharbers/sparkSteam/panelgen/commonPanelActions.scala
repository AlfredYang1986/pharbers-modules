package com.pharbers.panelgen.actionContainer

import com.pharbers.sparkSteam.paction.actionbase.pActionTrait
import com.pharbers.sparkSteam.paction.{excelReadingTrait, jarPreloadTrait}
import com.pharbers.sparkSteam.panelgen.actionContainer.pActionContainer

trait commonPanelActions extends pActionContainer {
    override val actions: List[pActionTrait] = jarPreloadTrait() :: excelReadingTrait("resource/test-01.xlsx") :: Nil
    override val actions: List[pActionTrait] = excelReadingTrait("/mnt/config/FileBase/235f39e3da85c2dee2a2b20d004a8b77/Client/170322安斯泰来1月底层检索.xls") :: Nil
}
