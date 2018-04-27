package com.pharbers.panel.pfizer

import java.util.UUID

import com.pharbers.common.excel.input.{PhExcelXLSXCommonFormat, PhXlsxSecondSheetFormat}
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.generalactions.{csv2DFAction, jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}
import com.pharbers.pactions.jobs.{sequenceJob, sequenceJobWithMap}
import com.pharbers.panel.common.phSavePanelJob
import com.pharbers.panel.panel_path_obj
import com.pharbers.panel.pfizer.actions.phPfizerPanelCommonAction
import com.pharbers.panel.pfizer.format.{phPfizerCpaFormat, phPfizerCpaSecondSheetFormat, phPfizerGycxFormat}

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

    val detailJobsMap: Map[String, pActionTrait] = Map(
        "AI_R_zith" -> phPfizerPanelCommonAction(df),
        "AI_S" -> phPfizerPanelCommonAction(df),
        "CNS_Z" -> phPfizerPanelCommonAction(df),
        "ELIQUIS" -> phPfizerPanelCommonAction(df),
        "INF" -> phPfizerPanelCommonAction(df),
        "LD" -> phPfizerPanelCommonAction(df),
        "ONC_other" -> phPfizerPanelCommonAction(df),
        "ONC_aml" -> phPfizerPanelCommonAction(df),
        "PAIN_lyrica" -> phPfizerPanelCommonAction(df),
        "Specialty_champix" -> phPfizerPanelCommonAction(df),
        "Specialty_other" -> phPfizerPanelCommonAction(df),
        "Urology_other" -> phPfizerPanelCommonAction(df),
        "Urology_viagra" -> phPfizerPanelCommonAction(df),

        "PAIN_other" -> phPfizerPanelCommonAction(df),
        "PAIN_C" -> phPfizerPanelCommonAction(df),
        "HTN" -> phPfizerPanelCommonAction(df),
        "HTN2" -> phPfizerPanelCommonAction(df),
        "AI_R_other" -> phPfizerPanelCommonAction(df),
        "AI_W" -> phPfizerPanelCommonAction(df),
        "AI_D" -> phPfizerPanelCommonAction(df),
        "ZYVOX" -> phPfizerPanelCommonAction(df),

        "DVP" -> phPfizerPanelCommonAction(df)
    )

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        phPfizerPreActions(mkt, temp_name).actions :::
        readCpa ::
        readNotArrivalHosp ::
        readGyc ::
        detailJobsMap.get(mkt).getOrElse(throw new Exception(s"undefined market=${mkt}")) ::
        phSavePanelJob(df) ::
        Nil
}
