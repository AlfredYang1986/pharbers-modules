package com.pharbers.panel.nhwa

import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.jobs.{choiceJob, sequenceJob, sequenceJobWithMap}
import com.pharbers.common.excel.input.{PhExcelXLSXCommonFormat, PhXlsxThirdSheetFormat}
import com.pharbers.pactions.generalactions.{csv2RddAction, existenceRdd, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat
import com.pharbers.panel.panel_path_obj

/**
  * 1. read 2017年未出版医院名单.xlsx
  * 2. read universe_麻醉市场_online.xlsx
  * 3. read 匹配表
  * 4. read 补充医院
  * 5. read 通用名市场定义, 读第三页
  */
object PhNhwaPreActions extends sequenceJobWithMap {
    override val name: String = "PhNhwaPreActions;"

    val match_dir: String = panel_path_obj.p_matchFilePath
    val cache_dir: String = panel_path_obj.p_cachePath + "nhwa/"

    val product_match_file: String = match_dir + "nhwa/匹配表.xlsx"
    val markets_match_file: String = match_dir + "nhwa/通用名市场定义.xlsx"
    val universe_file: String = match_dir + "nhwa/universe_麻醉市场_online.xlsx"
    val not_published_hosp_file: String = match_dir + "nhwa/2017年未出版医院名单.xlsx"
    val fill_hos_data_file: String = match_dir + "nhwa/补充医院.xlsx"

    override val actions: List[pActionTrait] =
        new choiceJob {
            override val name = "not_published_hosp_file"
            val actions: List[pActionTrait] = existenceRdd("not_published_hosp_file") ::
                    csv2RddAction(cache_dir + "/not_published_hosp_file") ::
                    new sequenceJob {
                        override val name: String = "read_not_published_hosp_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhExcelXLSXCommonFormat](not_published_hosp_file, "not_published_hosp_file") ::
                                    saveCurrenResultAction(cache_dir + "not_published_hosp_file") ::
                                    csv2RddAction(cache_dir + "not_published_hosp_file") :: Nil
                    } :: Nil
        } ::
        new choiceJob {
            override val name = "universe_file"
            val actions: List[pActionTrait] = existenceRdd("universe_file") ::
                    csv2RddAction(cache_dir + "universe_file") ::
                    new sequenceJob {
                        override val name: String = "read_universe_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhExcelXLSXCommonFormat](universe_file, "universe_file") ::
                                    saveCurrenResultAction(cache_dir + "universe_file") ::
                                    csv2RddAction(cache_dir + "universe_file") :: Nil
                    } :: Nil
        } ::
        new choiceJob{
            override val name = "product_match_file"
            val actions: List[pActionTrait] = existenceRdd("product_match_file") ::
                    csv2RddAction(cache_dir + "product_match_file") ::
                    new sequenceJob {
                        override val name: String = "read_product_match_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhExcelXLSXCommonFormat](product_match_file, "product_match_file") ::
                                    saveCurrenResultAction(cache_dir + "product_match_file") ::
                                    csv2RddAction(cache_dir + "product_match_file") :: Nil
                    } :: Nil
        } ::
        new choiceJob{
            override val name = "full_hosp_file"
            val actions: List[pActionTrait] = existenceRdd("full_hosp_file") ::
                    csv2RddAction(cache_dir + "full_hosp_file") ::
                    new sequenceJob {
                        override val name: String = "read_full_hosp_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[phNhwaCpaFormat](fill_hos_data_file, "full_hosp_file") ::
                                    saveCurrenResultAction(cache_dir + "full_hosp_file") ::
                                    csv2RddAction(cache_dir + "full_hosp_file") :: Nil
                    } :: Nil
        } ::
        new choiceJob {
            override val name = "markets_match_file"
            val actions: List[pActionTrait] = existenceRdd("markets_match_file") ::
                    csv2RddAction(cache_dir + "markets_match_file") ::
                    new sequenceJob {
                        override val name: String = "read_markets_match_file_job"
                        override val actions: List[pActionTrait] =
                            xlsxReadingAction[PhXlsxThirdSheetFormat](markets_match_file, "markets_match_file") ::
                                    saveCurrenResultAction(cache_dir + "markets_match_file") ::
                                    csv2RddAction(cache_dir + "markets_match_file") :: Nil
                    } :: Nil
        } :: Nil
}
