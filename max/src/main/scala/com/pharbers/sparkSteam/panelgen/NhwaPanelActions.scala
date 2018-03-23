package com.pharbers.sparkSteam.panelgen

import com.pharbers.panel.format.input.nhwa.PhExcelNhwaFormat
import com.pharbers.sparkSteam.paction.actionbase.pActionTrait
import com.pharbers.sparkSteam.paction.{excelReadingTrait, jarPreloadTrait, panelGenerateTrait, saveCurrenResultTrait}
import com.pharbers.sparkSteam.panelgen.actionContainer.pActionContainer

trait NhwaPanelActions extends pActionContainer {
    val file_local = "/mnt/config/FileBase/235f39e3da85c2dee2a2b20d004a8b77/Client/170322安斯泰来1月底层检索.xls"
    val file_local2 = "/mnt/config/FileBase/8ee0ca24796f9b7f284d931650edbd4b/Client/171215恩华2017年10月检索.xlsx"

    override val actions: List[pActionTrait] =
        jarPreloadTrait() ::
            excelReadingTrait[PhExcelNhwaFormat](file_local2) ::
            saveCurrenResultTrait("resource/result") ::
//            panelGenerateTrait("") ::
            Nil
}