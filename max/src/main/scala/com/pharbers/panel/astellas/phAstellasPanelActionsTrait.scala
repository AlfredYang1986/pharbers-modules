package com.pharbers.panel.astellas

import com.pharbers.pactions.generalactions._
import com.pharbers.panel.astellas.format._
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.actionContainer.pMapActionContainer

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

    override val actions: List[pActionTrait] = jarPreloadAction() ::
            xlsxReadingAction[phAstellasCpaFormat](cpa_file, "cpa") ::
            xlsxReadingAction[phAstellasGycxFormat](gycx_file, "gycx") ::
            xlsxReadingAction[phAstellasProductMatchFormat](product_match_file, "product_match_file") ::
            xlsxReadingAction[phAstellasMarketsMatchFormat](markets_match_file, "markets_match_file") ::
            xlsxReadingAction[phAstellasUniverseFormat](universe_file, "universe_file") ::
            xlsxReadingAction[phAstellasHospitalFormat](hospital_file, "hospital_file") ::
            phAstellasPanelImplAction(company, ym, mkt) ::
            saveMapResultAction("panelResult", panel_file) ::
            Nil
}