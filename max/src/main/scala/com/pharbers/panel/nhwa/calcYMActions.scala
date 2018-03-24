package com.pharbers.panel.nhwa

import com.pharbers.panel.common.calcYMAction
import com.pharbers.panel.nhwa.format.PhXlsxCpaFormat
import com.pharbers.paction.actionContainer.pActionContainer
import com.pharbers.paction.actionbase.{MapArgs, SingleArgFuncArgs, pActionTrait}
import com.pharbers.paction.format.input.common.PhExcelXLSXCommonFormat
import com.pharbers.paction.funcTrait.{xlsxReadingTrait, jarPreloadTrait, saveCurrenResultTrait}
import com.pharbers.panel.format.input.writable.nhwa.PhXlsxCpaWritable

trait calcYMActions extends pActionContainer {
    val cpa_location: String
    val result_localtion: String = "resource/result"

    val fym : PhXlsxCpaWritable => String = _.getRowKey("YM")
    val fc : PhXlsxCpaWritable => String = _.getRowKey("HOSPITAL_CODE")
    val m : MapArgs = MapArgs(Map(
        "fym" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fym),
        "fc" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fc)
    ))

    val file_local_10 = "resource/test-01.xlsx"
    val file_local_01 = "resource/test-03.xlsx"
    val file_local_all = "resource/test-02.xlsx"
    val file_local2 = "/mnt/config/FileBase/8ee0ca24796f9b7f284d931650edbd4b/Client/171215恩华2017年10月检索.xlsx"

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
                xlsxReadingTrait[PhXlsxCpaFormat](cpa_location) ::
                xlsxReadingTrait[PhExcelXLSXCommonFormat](file_local_all) ::
                calcYMAction(m) ::
                saveCurrenResultTrait(result_localtion) ::
                Nil
} 