package com.pharbers.panel.nhwa

import com.pharbers.panel.nhwa.format.PhXlsxCpaFormat
import com.pharbers.pactions.jobs.sequenceJob
import com.pharbers.panel.format.input.writable.nhwa.PhXlsxCpaWritable
import com.pharbers.pactions.actionbase.{MapArgs, SingleArgFuncArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.panel.astellas.phAstellasCalcYMImplAction
import com.pharbers.panel.panel_path_obj

import scala.collection.immutable.Map

object phNhwaCalcYMJob {
    def apply(args : Map[String, List[String]]) : phNhwaCalcYMJob = {
        new phNhwaCalcYMJob {
            // TODO : error code 需要整个工程都要对应
            lazy val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
            lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
            lazy val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
            lazy val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head

            override lazy val cpa_file: String = panel_path_obj.p_client_path + company + panel_path_obj.p_source_dir + cpa
            override lazy val cache_location: String = panel_path_obj.p_client_path + panel_path_obj.p_cache_dir + "ym/"
        }
    }
}

trait phNhwaCalcYMJob extends sequenceJob {
    val cpa_file: String
    val cache_location: String

    val fym : PhXlsxCpaWritable => String = _.getRowKey("YM")
    val fc : PhXlsxCpaWritable => String = _.getRowKey("HOSPITAL_CODE")
    val m : MapArgs = MapArgs(Map(
        "fym" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fym),
        "fc" -> SingleArgFuncArgs[PhXlsxCpaWritable, String](fc)
    ))

    override val actions: List[pActionTrait] = jarPreloadAction() ::
                xlsxReadingAction[PhXlsxCpaFormat](cpa_file) ::
                phAstellasCalcYMImplAction("") ::
                saveCurrenResultAction(cache_location) ::
                Nil
} 