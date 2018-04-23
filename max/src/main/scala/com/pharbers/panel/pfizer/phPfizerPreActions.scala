package com.pharbers.panel.pfizer

import com.pharbers.common.excel.input.{PhExcelXLSXCommonFormat, PhXlsxThirdSheetFormat}
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions.memory.phMemoryArgs
import com.pharbers.pactions.generalactions.{csv2DFAction, existenceRdd, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.pactions.jobs.{choiceJob, sequenceJob}
import com.pharbers.panel.panel_path_obj
import com.pharbers.panel.pfizer.format.phPfizerCpaFormat

/**
  * Created by jeorch on 18-4-18.
  */
case class phPfizerPreActions(temp_name: String) {
    implicit val companyArgs: phMemoryArgs = phMemoryArgs("Pfizer")
    val match_dir: String = panel_path_obj.p_matchFilePath
    val temp_dir: String = panel_path_obj.p_cachePath + temp_name + "/"

    val universe_file: String = match_dir + "pfizer/universe_INF_online.xlsx"
    val not_published_hosp_file: String = match_dir + "pfizer/2017年未出版医院名单.xlsx"
    val fill_hos_data_file: String = match_dir + "pfizer/补充医院.csv"
    val product_match_file: String = match_dir + "pfizer/产品标准化 vs IMS_Pfizer_6市场others.xlsx"
    val markets_match_file: String = match_dir + "pfizer/通用名市场定义.xlsx"
    val hos_prod_scope_file: String = match_dir + "pfizer/医院产品范围_MAX.xlsx"

    val actions: List[pActionTrait] =
        new choiceJob {
            override val name = "not_published_hosp_file"
            val actions: List[pActionTrait] = existenceRdd("not_published_hosp_file") ::
                csv2DFAction(temp_dir + "/not_published_hosp_file") ::
                new sequenceJob {
                    override val name: String = "read_not_published_hosp_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[PhExcelXLSXCommonFormat](not_published_hosp_file, "not_published_hosp_file") ::
                            saveCurrenResultAction(temp_dir + "not_published_hosp_file") ::
                            csv2DFAction(temp_dir + "not_published_hosp_file") :: Nil
                } :: Nil
        } ::
            new choiceJob {
                override val name = "universe_file"
                val actions: List[pActionTrait] = existenceRdd("universe_file") ::
                    csv2DFAction(temp_dir + "universe_file") ::
                    new sequenceJob {
                        override val name: String = "read_universe_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhExcelXLSXCommonFormat](universe_file, "universe_file") ::
                                saveCurrenResultAction(temp_dir + "universe_file") ::
                                csv2DFAction(temp_dir + "universe_file") :: Nil
                    } :: Nil
            } ::
            new choiceJob {
                override val name = "product_match_file"
                val actions: List[pActionTrait] = existenceRdd("product_match_file") ::
                    csv2DFAction(temp_dir + "product_match_file") ::
                    new sequenceJob {
                        override val name: String = "read_product_match_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhExcelXLSXCommonFormat](product_match_file, "product_match_file") ::
                                saveCurrenResultAction(temp_dir + "product_match_file") ::
                                csv2DFAction(temp_dir + "product_match_file") :: Nil
                    } :: Nil
            } ::
            new choiceJob {
                override val name = "full_hosp_file"
                val actions: List[pActionTrait] = existenceRdd("full_hosp_file") ::
                    csv2DFAction(temp_dir + "full_hosp_file") ::
                    new sequenceJob {
                        override val name: String = "read_full_hosp_file_job"
                        override val actions: List[pActionTrait] =
                                csv2DFAction(fill_hos_data_file) :: Nil
                    } :: Nil
            } ::
            new choiceJob {
                override val name = "markets_match_file"
                val actions: List[pActionTrait] = existenceRdd("markets_match_file") ::
                    csv2DFAction(temp_dir + "markets_match_file") ::
                    new sequenceJob {
                        override val name: String = "read_markets_match_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhXlsxThirdSheetFormat](markets_match_file, "markets_match_file") ::
                                saveCurrenResultAction(temp_dir + "markets_match_file") ::
                                csv2DFAction(temp_dir + "markets_match_file") :: Nil
                    } :: Nil
            } :: Nil
}

