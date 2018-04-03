package com.pharbers.panel.nhwa

import com.pharbers.panel.panel_path_obj
import com.pharbers.common.excel.input._
import com.pharbers.pactions.jobs.{sequenceJob, sequenceJobWithMap}
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat
import com.pharbers.pactions.generalactions.{jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}

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

    /**
      * 1. read 2017年未出版医院名单.xlsx
      */
    val cmd1 = xlsxReadingAction[PhExcelXLSXCommonFormat](not_published_hosp_file, "not_published_hosp_file")
    val cmd10 = saveCurrenResultAction(cache_location + cmd1.name)

    /**
      * 2. read universe_麻醉市场_online.xlsx
      */
    val cmd2 = xlsxReadingAction[PhExcelXLSXCommonFormat](universe_file, "universe_file")
    val cmd20 = saveCurrenResultAction(cache_location + cmd2.name)

    /**
      * 3. read 匹配表
      */
    val cmd3 = xlsxReadingAction[PhExcelXLSXCommonFormat](product_match_file, "product_match_file")
    val cmd30 = saveCurrenResultAction(cache_location + cmd3.name)

    /**
      * 4. read 补充医院
      */
    val cmd4 = xlsxReadingAction[phNhwaCpaFormat](full_hosp_file, "full_hosp_file")
    val cmd40 = saveCurrenResultAction(cache_location + cmd4.name)

    /**
      * 5. read 通用名市场定义, 读第三页
      */
    val cmd5 = xlsxReadingAction[PhXlsxThirdSheetFormat](markets_match_file, "markets_match_file")
    val cmd50 = saveCurrenResultAction(cache_location + cmd5.name)


    /**
      * 6. read CPA文件第一页
      */
    val cmd6 = xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa")
    val cmd60 = saveCurrenResultAction(cache_location + cmd6.name)

    /**
      * 7. read CPA文件第二页
      */
    val cmd7 = xlsxReadingAction[PhXlsxSecondSheetFormat](cpa_file, "not_arrival_hosp_file")
//    val cmd70 = saveCurrenResultAction(cache_location + cmd7.name)

    override val actions: List[pActionTrait] =
        jarPreloadAction() :: PhNhwaPreActions.actions :::
            (cmd6 :: cmd7 :: phNhwaPanelConcretJob(company, cache_location, ym, mkt) :: Nil)
}