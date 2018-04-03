package com.pharbers.panel.nhwa

import com.pharbers.panel.panel_path_obj
import com.pharbers.common.excel.input._
import com.pharbers.pactions.jobs.{sequenceJob, sequenceJobWithMap}
import com.pharbers.pactions.actionbase.{ListArgs, MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{csv2RddAction, jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat

object phNhwaPanelJob {

//    def apply(args : Map[String, List[String]])(_ym: List[String], _mkt: String) : phNhwaPanelJob = {
//        new phNhwaPanelJob {
//            override val ym: List[String] = _ym
//            override val mkt: String = _mkt
//
//            lazy val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
//            override lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
//            lazy val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
//            lazy val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head
//
//            override lazy val cpa_file = panel_path_obj.p_base_path + company + panel_path_obj.p_source_dir + cpa
//
//            override lazy val product_match_file = panel_path_obj.p_base_path + company + NhwaFilePath.p_product_match_file
//            override lazy val markets_match_file = panel_path_obj.p_base_path + company + NhwaFilePath.p_markets_match_file
//            override lazy val universe_file = panel_path_obj.p_base_path + company + panel_path_obj.p_universe_file.replace("##market##", mkt)
//            override lazy val not_published_hosp_file = panel_path_obj.p_base_path + company + NhwaFilePath.p_not_published_hosp_file
//            override lazy val full_hosp_file = panel_path_obj.p_base_path + company + NhwaFilePath.p_fill_hos_data_file
//
//            override lazy val cache_location = panel_path_obj.p_base_path + panel_path_obj.p_cache_dir
//        }
//    }

    def apply(arg_cpa : String, arg_mid : String, arg_dest: String) : phNhwaPanelJob = {
        new phNhwaPanelJob {
            override lazy val cpa_file: String = arg_cpa
            override lazy val mid_dir : String = arg_mid
            override lazy val cache_location: String = arg_dest
        }
    }
}

/**
  * 6. read CPA文件第一页
  * 7. read CPA文件第二页
  */

trait phNhwaPanelJob extends sequenceJobWithMap {
    val cpa_file: String
    val mid_dir : String
    val cache_location: String

    val cmd6 = new sequenceJob {
                    name = "cpa"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                            saveCurrenResultAction(mid_dir + "/cpa") ::
                                csv2RddAction(mid_dir + "/cpa", "cpa") :: Nil
               }

    val cmd7 = new sequenceJob {
                    name = "not_arrival_hosp_file"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[PhXlsxSecondSheetFormat](cpa_file, "not_arrival_hosp_file") ::
                            saveCurrenResultAction(mid_dir + "/not_arrival_hosp_file") ::
                                csv2RddAction(mid_dir + "/not_arrival_hosp_file", "not_arrival_hosp_file") :: Nil
               }

    val df = MapArgs(
        Map(
            "des" -> StringArgs(cache_location),
            "ym" -> ListArgs(StringArgs("201701") :: Nil),
            "mkt" -> StringArgs("麻醉市场")
        )
    )

    override val actions: List[pActionTrait] =
        jarPreloadAction() :: PhNhwaPreActions.actions :::
            (cmd6 :: cmd7 :: phNhwaPanelConcretJob(df) :: Nil)
}