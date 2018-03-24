package com.pharbers.panel.astellas

import com.pharbers.panel.common.calcYMAction
import com.pharbers.panel.astellas.format.PhXlsCpaFormat
import com.pharbers.paction.actionContainer.pActionContainer
import com.pharbers.panel.format.input.writable.astellas.PhXlsCpaWritable
import com.pharbers.paction.actionbase.{MapArgs, SingleArgFuncArgs, pActionTrait}
import com.pharbers.paction.funcTrait.{jarPreloadTrait, saveCurrenResultTrait, xlsReadingTrait}

trait calcYMActions extends pActionContainer {
    val cpa_location: String
    val result_localtion: String = "resource/result"

    val fym : PhXlsCpaWritable => String = _.getRowKey("YM")
    val fc : PhXlsCpaWritable => String = _.getRowKey("HOSPITAL_CODE")
    val m : MapArgs = MapArgs(Map(
        "fym" -> SingleArgFuncArgs[PhXlsCpaWritable, String](fym),
        "fc" -> SingleArgFuncArgs[PhXlsCpaWritable, String](fc)
    ))

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
                xlsReadingTrait[PhXlsCpaFormat](cpa_location) ::
                calcYMAction(m) ::
                saveCurrenResultTrait(result_localtion) ::
                Nil
} 