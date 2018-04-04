package com.pharbers.panel.nhwa

import scala.collection.immutable.Map
import com.pharbers.panel.panel_path_obj
import com.pharbers.pactions.jobs.sequenceJob
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat
import com.pharbers.pactions.generalactions.{jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.panel.astellas.phAstellasCalcYMImplAction
import scala.collection.immutable.Map

object phNhwaCalcYMJob {
//    def apply(args : Map[String, List[String]]) : phNhwaCalcYMJob = {
//        new phNhwaCalcYMJob {
//            // TODO : error code 需要整个工程都要对应
//            lazy val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
//            lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
//            lazy val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
//            lazy val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head
//
//            override lazy val cpa_file: String = panel_path_obj.p_client_path + company + panel_path_obj.p_source_dir + cpa
//            override lazy val cache_location: String = panel_path_obj.p_client_path + panel_path_obj.p_cache_dir + "ym/"
//        }
//    }

    def apply(cpa_path : String, des : String) : phNhwaCalcYMJob = {
        new phNhwaCalcYMJob {
            override lazy val cpa_file: String = cpa_path
            override lazy val cache_location: String = des // TODO : 以后从文件系统中自动hash生成
        }
    }
}

trait phNhwaCalcYMJob extends sequenceJob {
    override val name: String = ""
    val cpa_file: String
    val cache_location: String

    override val actions: List[pActionTrait] = jarPreloadAction() ::
                xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                phNhwaCalcYMConcretJob() ::
                saveCurrenResultAction(cache_location) ::
                Nil
} 