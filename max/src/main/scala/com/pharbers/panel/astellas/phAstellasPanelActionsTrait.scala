package com.pharbers.panel.astellas

import com.pharbers.pactions.generalactions._
import com.pharbers.panel.astellas.format._
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.panel.format.input.writable.PhExcelWritable

trait phAstellasPanelActionsTrait extends sequenceJobWithMap {
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
//            saveMapResultAction("cpa", panel_file + "cpa") ::
            xlsxReadingAction[phAstellasGycxFormat](gycx_file, "gycx") ::
//            saveMapResultAction("gycx", panel_file + "gycx") ::
            xlsxReadingAction[phAstellasProductMatchFormat](product_match_file, "product_match_file") ::
//            saveMapResultAction("product_match_file", panel_file + "pmf") ::
            xlsxReadingAction[phAstellasMarketsMatchFormat](markets_match_file, "markets_match_file") ::
//            saveMapResultAction("markets_match_file", panel_file + "mmf") ::
            xlsxReadingAction[phAstellasUniverseFormat](universe_file, "universe_file") ::
//            saveMapResultAction("universe_file", panel_file + "uf") ::
            xlsxReadingAction[phAstellasHospitalFormat](hospital_file, "hospital_file") ::
//            saveMapResultAction("hospital_file", panel_file + "hf") ::
            phAstellasPanelImplAction(company, ym, mkt) ::
            saveMapResultAction[PhExcelWritable]("panelResult", panel_file, ".csv") ::
            Nil
}