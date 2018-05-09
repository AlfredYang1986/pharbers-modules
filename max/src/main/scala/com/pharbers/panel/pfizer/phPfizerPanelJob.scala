package com.pharbers.panel.pfizer

import java.util.UUID
import com.pharbers.panel.panel_path_obj
import com.pharbers.panel.pfizer.actions._
import com.pharbers.panel.common.phSavePanelJob
import com.pharbers.common.excel.input.PhXlsxSecondSheetFormat
import com.pharbers.pactions.jobs.{sequenceJob, sequenceJobWithMap}
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.panel.pfizer.format.{phPfizerCpaFormat, phPfizerGycxFormat}
import com.pharbers.pactions.generalactions.{csv2DFAction, jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}

/**
  * Created by jeorch on 18-4-18.
  */
object phPfizerPanelJob {

    def apply(arg_cpa: String, arg_gyc: String, arg_ym: String, arg_mkt: String) : phPfizerPanelJob = {
        new phPfizerPanelJob {
            override lazy val cpa_file: String = arg_cpa
            override lazy val gyc_file: String = arg_gyc
            override lazy val ym: String = arg_ym
            override lazy val mkt: String = arg_mkt

            override lazy val temp_name: String = UUID.randomUUID().toString
        }
    }
}

/**
  * 1. read CPA文件第一页
  * 2. read CPA文件第二页
  * 3. read GYCX文件
  */
trait phPfizerPanelJob extends sequenceJobWithMap {
    override val name: String = "phPfizerPanelJob"

    val ym: String
    val mkt: String
    val cpa_file: String
    val gyc_file: String
    val temp_name: String
    val temp_dir: String = panel_path_obj.p_cachePath + temp_name + "/"

    /**
      * 1. read CPA文件第一页
      */
    val readCpa = new sequenceJob {
        override val name = "cpa"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phPfizerCpaFormat](cpa_file, "cpa") ::
                saveCurrenResultAction(temp_dir + "cpa") ::
                csv2DFAction(temp_dir + "cpa") :: Nil
    }

    /**
      * 2. read CPA文件第二页
      */
    val readNotArrivalHosp = new sequenceJob {
        override val name = "not_arrival_hosp_file"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhXlsxSecondSheetFormat](cpa_file, "not_arrival_hosp_file") ::
                saveCurrenResultAction(temp_dir + "not_arrival_hosp_file") ::
                csv2DFAction(temp_dir + "not_arrival_hosp_file") :: Nil
    }

    /**
      * 3. read GYCX文件
      */
    val readGyc = new sequenceJob {
        override val name = "gyc"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phPfizerGycxFormat](gyc_file, "gyc") ::
                saveCurrenResultAction(temp_dir + "gyc") ::
                csv2DFAction(temp_dir + "gyc") :: Nil
    }

    val df = MapArgs(
        Map(
            "ym" -> StringArgs(ym),
            "mkt" -> StringArgs(mkt),
            "name" -> StringArgs(temp_name)
        )
    )

    val splitMktJobsMap: Map[String, pActionTrait] = Map(
        "AI_R_zith" -> phPfizerPanelNoSplitAction(df),
        "AI_S" -> phPfizerPanelNoSplitAction(df),
        "CNS_Z" -> phPfizerPanelNoSplitAction(df),
        "ELIQUIS" -> phPfizerPanelNoSplitAction(df),
        "INF" -> phPfizerPanelNoSplitAction(df),
        "LD" -> phPfizerPanelNoSplitAction(df),
        "ONC_other" -> phPfizerPanelNoSplitAction(df),
        "ONC_aml" -> phPfizerPanelNoSplitAction(df),
        "PAIN_lyrica" -> phPfizerPanelNoSplitAction(df),
        "Specialty_champix" -> phPfizerPanelNoSplitAction(df),
        "Specialty_other" -> phPfizerPanelNoSplitAction(df),
        "Urology_other" -> phPfizerPanelNoSplitAction(df),
        "Urology_viagra" -> phPfizerPanelNoSplitAction(df),

        "PAIN_C" -> phPfizerPanelSplitChildMarketAction(df),
        "HTN2" -> phPfizerPanelSplitChildMarketAction(df),
        "AI_D" -> phPfizerPanelSplitChildMarketAction(df),
        "ZYVOX" -> phPfizerPanelSplitChildMarketAction(df),

        "PAIN_other" -> phPfizerPanelSplitFatherMarketAction(df),
        "HTN" -> phPfizerPanelSplitFatherMarketAction(df),
        "AI_W" -> phPfizerPanelSplitFatherMarketAction(df),
        "AI_R_other" -> phPfizerPanelSplitFatherMarketAction(df),

        "CNS_R" -> phPfizerPanelNoSplitAction(df),
        "DVP" -> phPfizerPanelNoSplitAction(df)
    )

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        phPfizerPreActions(mkt, temp_name).actions :::
        readCpa ::
        readNotArrivalHosp ::
        readGyc ::
        splitMktJobsMap.get(mkt).getOrElse(throw new Exception(s"undefined market=${mkt}")) ::
        phPfizerPanelCommonAction(df) ::
        phSavePanelJob(df) ::
        Nil
}
