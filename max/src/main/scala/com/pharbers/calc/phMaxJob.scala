package com.pharbers.calc

import java.util.UUID
import akka.actor.Actor
import com.pharbers.calc.actions._
import com.pharbers.pactions.jobs._
import com.pharbers.channel.sendEmTrait
import com.pharbers.pactions.generalactions._
import com.pharbers.common.algorithm.max_path_obj
import org.apache.spark.listener.progress.sendMultiProgress
import com.pharbers.common.excel.input.PhExcelXLSXCommonFormat
import org.apache.spark.listener.{MaxSparkListener, addListenerAction}
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}

object phMaxJob {
    def apply(_company: String, _user: String, _job_id: String)
             (_ym: String, _mkt: String, panel: String, universe: String, _p_current: Int, _p_total: Int)
             (implicit _actor: Actor): phMaxJob = {
        new phMaxJob {
            override lazy val ym: String = _ym
            override lazy val mkt: String = _mkt
            override lazy val panel_name: String = panel
            override lazy val universe_name: String = universe

            override lazy val user: String = _user
            override lazy val company: String = _company
            override lazy val job_id: String = _job_id
            override lazy val actor: Actor = _actor
            override lazy val p_total: Double = _p_total
            override lazy val p_current: Double = _p_current
        }
    }
}


trait phMaxJob extends sequenceJobWithMap {
    override val name: String = "phMaxCalcJob"

    val ym: String
    val mkt: String
    val panel_name: String
    val universe_name: String

    val user, company, job_id: String
    val p_current, p_total: Double
    implicit val actor: Actor
    implicit val mp: (sendEmTrait, Double) => Unit = sendMultiProgress(company, user)(p_current, p_total).multiProgress

    val panel_file: String = max_path_obj.p_panelPath + panel_name
    val universe_file: String = max_path_obj.p_matchFilePath + universe_name
    val temp_dir: String = max_path_obj.p_cachePath + panel_name + "/"

    // 1. load panel data
    val loadPanelData: sequenceJob = new sequenceJob {
        override val name: String = "panel_data"
        override val actions: List[pActionTrait] = csv2DFAction(panel_file) :: Nil
    }

    // 留做测试 1. load panel data of xlsx
    val loadPanelDataOfExcel: sequenceJob = new sequenceJob {
        val temp_panel_name: String = UUID.randomUUID().toString
        override val name = "panel_data"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhExcelXLSXCommonFormat](panel_file, temp_panel_name) ::
                saveCurrenResultAction(temp_dir + temp_panel_name) ::
                csv2DFAction(temp_dir + temp_panel_name) :: Nil
    }

    // 2. read universe file
    val universe_cache: String = UUID.randomUUID().toString
    val readUniverseFile: sequenceJob = new sequenceJob {
        override val name = "read_universe_file_job"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhExcelXLSXCommonFormat](universe_file, universe_cache) ::
                    saveCurrenResultAction(temp_dir + universe_cache) ::
                    csv2DFAction(temp_dir + universe_cache) :: Nil
    }

    val df = MapArgs(
        Map(
            "ym" -> StringArgs(ym),
            "mkt" -> StringArgs(mkt),
            "user" -> StringArgs(user),
            "name" -> StringArgs(panel_name),
            "company" -> StringArgs(company),
            "job_id" -> StringArgs(job_id)
        )
    )

    override val actions: List[pActionTrait] = { jarPreloadAction() ::
                setLogLevelAction("ERROR") ::
                addListenerAction(MaxSparkListener(0, 5)) ::
                loadPanelData ::
//                loadPanelDataOfExcel ::
                addListenerAction(MaxSparkListener(6, 10)) ::
                readUniverseFile ::
                addListenerAction(MaxSparkListener(11, 80)) ::
                phMaxCalcAction() ::
                addListenerAction(MaxSparkListener(81, 90)) ::
                phMaxPersistentAction(df) ::
                phMaxInfo2RedisAction(df) ::
                addListenerAction(MaxSparkListener(91, 99)) ::
                phMaxResult2MongoAction() ::
                Nil
    }
}