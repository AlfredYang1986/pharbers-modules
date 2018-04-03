package com.pharbers.panel.nhwa

import com.pharbers.panel.panel_path_obj
import com.pharbers.common.excel.input._
import com.pharbers.pactions.jobs.{sequenceJob, sequenceJobWithMap}
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions.{csv2RddAction, jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat

object phNhwaPanelJob {

    def apply(args : Map[String, List[String]])(_ym: List[String], _mkt: String) : phNhwaPanelJob = {
        new phNhwaPanelJob {
            override val ym: List[String] = _ym
            override val mkt: String = _mkt

            lazy val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
            override lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
            lazy val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
            lazy val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head

            override lazy val cpa_file = panel_path_obj.p_base_path + company + panel_path_obj.p_source_dir + cpa

            override lazy val product_match_file = panel_path_obj.p_base_path + company + NhwaFilePath.p_product_match_file
            override lazy val markets_match_file = panel_path_obj.p_base_path + company + NhwaFilePath.p_markets_match_file
            override lazy val universe_file = panel_path_obj.p_base_path + company + panel_path_obj.p_universe_file.replace("##market##", mkt)
            override lazy val not_published_hosp_file = panel_path_obj.p_base_path + company + NhwaFilePath.p_not_published_hosp_file
            override lazy val full_hosp_file = panel_path_obj.p_base_path + company + NhwaFilePath.p_fill_hos_data_file

            override lazy val cache_location = panel_path_obj.p_base_path + panel_path_obj.p_cache_dir
        }
    }
}

/**
  * 6. read CPA文件第一页
  * 7. read CPA文件第二页
  */

trait phNhwaPanelJob extends sequenceJobWithMap {
    val company: String
    val cpa_file: String
    val ym: List[String]
    val mkt: String

    val product_match_file: String
    val markets_match_file: String
    val universe_file: String
    val not_published_hosp_file: String
    val full_hosp_file: String

    val cache_location: String

    val cmd6 = xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                    saveCurrenResultAction(cache_location + "cpa") ::
                        csv2RddAction(cache_location + "/cpa") :: Nil

    val cmd7 = xlsxReadingAction[PhXlsxSecondSheetFormat](cpa_file, "not_arrival_hosp_file") ::
                    saveCurrenResultAction(cache_location + "not_arrival_hosp_file") :: Nil

    override val actions: List[pActionTrait] =
        jarPreloadAction() :: PhNhwaPreActions.actions :::
            (cmd6 ::: cmd7 ::: phNhwaPanelConcretJob(company, cache_location, ym, mkt) :: Nil)
}