package com.pharbers.panel.astellas

import scala.collection.immutable.Map
import com.pharbers.panel.commonAction.calcYMAction
import com.pharbers.panel.astellas.format.PhXlsxCpaFormat
import com.pharbers.paction.actionContainer.pActionContainer
import com.pharbers.panel.format.input.writable.astellas.PhXlsxCpaWritable
import com.pharbers.paction.actionbase.{MapArgs, SingleArgFuncArgs, pActionTrait}
import com.pharbers.paction.funcTrait.{jarPreloadTrait, saveCurrenResultTrait, xlsxReadingTrait}


trait phAstellasCalcYMActionsTrait extends pActionContainer {
    val cpa_file: String
    val cache_location: String

    val fym : PhXlsxCpaWritable => String = _.getRowKey("YM")
    val fc : PhXlsxCpaWritable => String = _.getRowKey("HOSPITAL_CODE")
    val m : MapArgs = MapArgs(Map(
        "fym" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fym),
        "fc" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fc)
    ))

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
                xlsxReadingTrait[PhXlsxCpaFormat](cpa_file) ::
                calcYMAction(m) ::
                saveCurrenResultTrait(cache_location) ::
                Nil
} 