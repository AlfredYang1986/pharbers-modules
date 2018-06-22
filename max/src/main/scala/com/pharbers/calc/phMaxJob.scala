package com.pharbers.calc

import java.util.UUID

import akka.actor.Actor
import com.pharbers.calc.actions._
import com.pharbers.channel.util.sendEmTrait
import com.pharbers.pactions.jobs._
import com.pharbers.pactions.generalactions._
import com.pharbers.common.algorithm.max_path_obj
import org.apache.spark.listener.progress.sendMultiProgress
import com.pharbers.pactions.excel.input.PhExcelXLSXCommonFormat
import org.apache.spark.listener.{MaxSparkListener, addListenerAction}
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}

case class phMaxJob(args: Map[String, String])(implicit _actor: Actor) extends sequenceJobWithMap {
    override val name: String = "phMaxCalcJob"

    val panel_name = args("panel_name")

    val panel_file: String = max_path_obj.p_panelPath + panel_name
    val universe_file: String = max_path_obj.p_matchFilePath + args("universe_file")
    val temp_dir: String = max_path_obj.p_cachePath + panel_name + "/"

    lazy val ym: String = args("ym")
    lazy val mkt: String = args("mkt")
    lazy val user: String = args("user_id")
    lazy val job_id: String = args("job_id")
    lazy val company: String = args("company_id")
    lazy val p_total: Double = args("p_total").toDouble
    lazy val p_current: Double = args("p_current").toDouble

    implicit val mp: (sendEmTrait, Double, String) => Unit = sendMultiProgress(company, user, "calc")(p_current, p_total).multiProgress

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
    val universe_cache: String = panel_name + UUID.randomUUID().toString
    val readUniverseFile: sequenceJob = new sequenceJob {
        override val name = "universe_data"
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
                readUniverseFile ::
                phMaxCalcAction() ::
                addListenerAction(MaxSparkListener(6, 40)) ::
                phMaxPersistentAction(df) ::
                addListenerAction(MaxSparkListener(41, 90)) ::
                phMaxInfo2RedisAction(df) ::
                Nil
    }

}