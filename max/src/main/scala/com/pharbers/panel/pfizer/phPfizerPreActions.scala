package com.pharbers.panel.pfizer

import com.pharbers.common.excel.input.PhExcelXLSXCommonFormat
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions.memory.phMemoryArgs
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.jobs.{choiceJob, sequenceJob}
import com.pharbers.panel.panel_path_obj
import com.pharbers.panel.pfizer.actions.phPfizerFillHospitalRdd2DfAction

/**
  * Created by jeorch on 18-4-18.
  */
case class phPfizerPreActions(mkt: String, temp_name: String) {
    implicit val companyArgs: phMemoryArgs = phMemoryArgs("Pfizer")
    val match_dir: String = panel_path_obj.p_matchFilePath
    val temp_dir: String = panel_path_obj.p_cachePath + temp_name + "/"

    val universe_file: String = match_dir + s"pfizer/universe_${mkt}_online.xlsx"
    /**
      * 不同年份有不同的补充文件,是否需要进行历史补充医院的合并?
      * ToDo:为了满足用户不仅仅可以计算当月,还可以计算历史月份
      */
    val fill_hos_data_file: String = match_dir + "pfizer/补充医院utf8_2018.txt"
    val product_match_file: String = match_dir + "pfizer/产品标准化+vs+IMS_Pfizer_6市场others_0329.xlsx"
    val markets_match_file: String = match_dir + "pfizer/通用名市场定义_0502.xlsx"
    val pfc_match_file: String = match_dir + "pfizer/PACKID生成panel.xlsx"

    val actions: List[pActionTrait] =
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
                    phPfizerFillHospitalRdd2DfAction(temp_dir + "full_hosp_file") ::
                    new sequenceJob {
                        override val name: String = "read_full_hosp_file_job"
                        override val actions: List[pActionTrait] =
                            txt2RDDAction(fill_hos_data_file) ::
                                saveCurrenResultAction(temp_dir + "full_hosp_file") ::
                                phPfizerFillHospitalRdd2DfAction(temp_dir + "full_hosp_file") :: Nil
                    } :: Nil
            } ::
            new choiceJob {
                override val name = "markets_match_file"
                val actions: List[pActionTrait] = existenceRdd("markets_match_file") ::
                    csv2DFAction(temp_dir + "markets_match_file") ::
                    new sequenceJob {
                        override val name: String = "read_markets_match_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhExcelXLSXCommonFormat](markets_match_file, "markets_match_file") ::
                                saveCurrenResultAction(temp_dir + "markets_match_file") ::
                                csv2DFAction(temp_dir + "markets_match_file") :: Nil
                    } :: Nil
            } ::
            new choiceJob {
                override val name = "pfc_match_file"
                val actions: List[pActionTrait] = existenceRdd("pfc_match_file") ::
                    csv2DFAction(temp_dir + "pfc_match_file") ::
                    new sequenceJob {
                        override val name: String = "read_pfc_match_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhExcelXLSXCommonFormat](pfc_match_file, "pfc_match_file") ::
                                saveCurrenResultAction(temp_dir + "pfc_match_file") ::
                                csv2DFAction(temp_dir + "pfc_match_file") :: Nil
                    } :: Nil
            } :: Nil

}

