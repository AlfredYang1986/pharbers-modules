package com.pharbers.sparkSteam.panelgen

import com.pharbers.panel.format.input.nhwa.PhExcelNhwaFormat
import com.pharbers.panel.format.input.writable.nhwa.PhExcelNhwaWritable
import com.pharbers.sparkSteam.paction.actionbase.{MapArgs, SingleArgFuncArgs, pActionTrait}
import com.pharbers.sparkSteam.paction.{calcYMTrait2, excelReadingTrait, jarPreloadTrait, saveCurrenResultTrait}
import com.pharbers.sparkSteam.panelgen.actionContainer.pActionContainer

trait NhwaYMActions extends pActionContainer {

    val fy : PhExcelNhwaWritable => String = _.getRowKey("YEAR")
    val fm : PhExcelNhwaWritable => String = _.getRowKey("MONTH")
    val m : MapArgs = new MapArgs(Map(
            "fy" -> new SingleArgFuncArgs[PhExcelNhwaWritable, String](fy),
            "fm" -> new SingleArgFuncArgs[PhExcelNhwaWritable, String](fm)
        ))

    override val actions: List[pActionTrait] =
        jarPreloadTrait() ::
            excelReadingTrait[PhExcelNhwaFormat]("resource/test-01.xlsx") ::
            calcYMTrait2(m) ::
            saveCurrenResultTrait("resource/result") ::
            Nil
}
