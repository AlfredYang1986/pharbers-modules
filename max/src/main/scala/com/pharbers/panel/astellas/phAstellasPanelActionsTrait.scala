package com.pharbers.panel.astellas

import com.pharbers.paction.funcTrait._
import com.pharbers.panel.astellas.format._
import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.paction.actionContainer.pMapActionContainer

trait phAstellasPanelActionsTrait extends pMapActionContainer {
    val company: String
    val cpa_file: String
    val gycx_file: String
    val ym: List[String]
    val mkt: String

    val product_match_file: String
    val markets_match_file: String
    val universe_file: String
    val hospital_file: String
    val panel_file: String

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
            xlsxReadingTrait[phAstellasCpaFormat](cpa_file, "cpa") ::
            xlsxReadingTrait[phAstellasGycxFormat](gycx_file, "gycx") ::
            xlsxReadingTrait[phAstellasProductMatchFormat](product_match_file, "product_match_file") ::
            xlsxReadingTrait[phAstellasMarketsMatchFormat](markets_match_file, "markets_match_file") ::
            xlsxReadingTrait[phAstellasUniverseFormat](universe_file, "universe_file") ::
            xlsxReadingTrait[phAstellasHospitalFormat](hospital_file, "hospital_file") ::
            phAstellasPanelImplAction(company, ym, mkt) ::
            saveMapResultTrait("panelResult", panel_file) ::
            Nil
}