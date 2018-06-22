package com.pharbers.panel.pfizer

import java.util.UUID

import akka.actor.Actor
import com.pharbers.channel.util.sendEmTrait
import com.pharbers.panel.pfizer.actions._
import com.pharbers.pactions.generalactions._
import com.pharbers.common.algorithm.max_path_obj
import org.apache.spark.listener.progress.sendMultiProgress
import com.pharbers.pactions.generalactions.memory.phMemoryArgs
import com.pharbers.panel.common.{phPanelInfo2Redis, phSavePanelJob}
import org.apache.spark.listener.{MaxSparkListener, addListenerAction}
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}
import com.pharbers.pactions.jobs.{choiceJob, sequenceJob, sequenceJobWithMap}
import com.pharbers.panel.pfizer.format.{phPfizerCpaFormat, phPfizerGycxFormat}
import com.pharbers.pactions.excel.input.{PhExcelXLSXCommonFormat, PhXlsxSecondSheetFormat}

/**
  * Created by jeorch on 18-4-18.
  *     Modify by clock on 18-5-21
  */
case class phPfizerPanelJob(args: Map[String, String])(implicit _actor: Actor) extends sequenceJobWithMap {
    override val name: String = "phPfizerPanelJob"

    val temp_name: String = UUID.randomUUID().toString
    val temp_dir: String = max_path_obj.p_cachePath + temp_name + "/"
    val match_dir: String = max_path_obj.p_matchFilePath
    val source_dir: String = max_path_obj.p_clientPath

    val universe_file: String = match_dir + args("universe_file")
    val product_match_file: String = match_dir + args("product_match_file")
    val markets_match_file: String = match_dir + args("markets_match_file")
    /**
      * 不同年份有不同的补充文件,是否需要进行历史补充医院的合并?
      * ToDo:为了满足用户不仅仅可以计算当月,还可以计算历史月份
      */
    val fill_hos_data_file: String = match_dir + args("fill_hos_data_file")
    val pfc_match_file: String = match_dir + args("pfc_match_file")
    val cpa_file: String = source_dir + args("cpa")
    val gyc_file: String = source_dir + args("gycx")

    lazy val ym: String = args("ym")
    lazy val mkt: String = args("mkt")
    lazy val user: String = args("user_id")
    lazy val job_id: String = args("job_id")
    lazy val company: String = args("company_id")
    lazy val p_total: Double = args("p_total").toDouble
    lazy val p_current: Double = args("p_current").toDouble

    implicit val companyArgs: phMemoryArgs = phMemoryArgs(company)
    implicit val mp: (sendEmTrait, Double, String) => Unit = sendMultiProgress(company, user, "panel")(p_current, p_total).multiProgress


    //1. read universe_file
    val load_universe_file: choiceJob = new choiceJob {
        override val name = "universe_file"
        val actions: List[pActionTrait] = existenceRdd("universe_file") ::
                csv2DFAction(temp_dir + "universe_file") ::
                new sequenceJob {
                    override val name: String = "read_universe_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[PhExcelXLSXCommonFormat](universe_file, "universe_file") ::
                                saveCurrenResultAction(temp_dir + "universe_file") ::
                                csv2DFAction(temp_dir + "universe_file") :: Nil
                } :: Nil
    }

    //2. read product_match_file
    val load_product_match_file: choiceJob = new choiceJob {
        override val name = "product_match_file"
        val actions: List[pActionTrait] = existenceRdd("product_match_file") ::
                csv2DFAction(temp_dir + "product_match_file") ::
                new sequenceJob {
                    override val name: String = "read_product_match_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[PhExcelXLSXCommonFormat](product_match_file, "product_match_file") ::
                                saveCurrenResultAction(temp_dir + "product_match_file") ::
                                csv2DFAction(temp_dir + "product_match_file") :: Nil
                } :: Nil
    }

    //3. read markets_match_file
    val load_markets_match_file: choiceJob = new choiceJob {
        override val name = "markets_match_file"
        val actions: List[pActionTrait] = existenceRdd("markets_match_file") ::
                csv2DFAction(temp_dir + "markets_match_file") ::
                new sequenceJob {
                    override val name: String = "read_markets_match_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[PhExcelXLSXCommonFormat](markets_match_file, "markets_match_file") ::
                                saveCurrenResultAction(temp_dir + "markets_match_file") ::
                                csv2DFAction(temp_dir + "markets_match_file") :: Nil
                } :: Nil
    }

    //4. read full_hosp_file
    val load_full_hosp_file: choiceJob = new choiceJob {
        override val name = "full_hosp_file"
        val actions: List[pActionTrait] = existenceRdd("full_hosp_file") ::
                phPfizerFillHospitalRdd2DfAction(temp_dir + "full_hosp_file") ::
                new sequenceJob {
                    override val name: String = "read_full_hosp_file_job"
                    override val actions: List[pActionTrait] =
                        txt2RDDAction(fill_hos_data_file) ::
                                saveCurrenResultAction(temp_dir + "full_hosp_file") ::
                                phPfizerFillHospitalRdd2DfAction(temp_dir + "full_hosp_file") :: Nil
                } :: Nil
    }

    //5. read pfc_match_file
    val load_pfc_match_file: choiceJob = new choiceJob {
        override val name = "pfc_match_file"
        val actions: List[pActionTrait] = existenceRdd("pfc_match_file") ::
                csv2DFAction(temp_dir + "pfc_match_file") ::
                new sequenceJob {
                    override val name: String = "read_pfc_match_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[PhExcelXLSXCommonFormat](pfc_match_file, "pfc_match_file") ::
                                saveCurrenResultAction(temp_dir + "pfc_match_file") ::
                                csv2DFAction(temp_dir + "pfc_match_file") :: Nil
                } :: Nil
    }

    //6. read CPA文件第一页
    val readCpa: sequenceJob = new sequenceJob {
        override val name = "cpa"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phPfizerCpaFormat](cpa_file, "cpa") ::
                saveCurrenResultAction(temp_dir + "cpa") ::
                csv2DFAction(temp_dir + "cpa") :: Nil
    }

    //7. read CPA文件第二页
    val readNotArrivalHosp: sequenceJob = new sequenceJob {
        override val name = "not_arrival_hosp_file"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhXlsxSecondSheetFormat](cpa_file, "not_arrival_hosp_file") ::
                saveCurrenResultAction(temp_dir + "not_arrival_hosp_file") ::
                csv2DFAction(temp_dir + "not_arrival_hosp_file") :: Nil
    }

    //8. read GYCX文件
    val readGyc: sequenceJob = new sequenceJob {
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
            "user" -> StringArgs(user),
            "name" -> StringArgs(temp_name),
            "company" -> StringArgs(company),
            "job_id" -> StringArgs(job_id)
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

    override val actions: List[pActionTrait] = { jarPreloadAction() ::
            setLogLevelAction("ERROR") ::
            addListenerAction(MaxSparkListener(0, 10)) ::
            load_universe_file ::
            addListenerAction(MaxSparkListener(11, 20)) ::
            load_product_match_file ::
            addListenerAction(MaxSparkListener(21, 30)) ::
            load_markets_match_file ::
            addListenerAction(MaxSparkListener(31, 40)) ::
            load_full_hosp_file ::
            addListenerAction(MaxSparkListener(41, 50)) ::
            load_pfc_match_file ::
            addListenerAction(MaxSparkListener(51, 60)) ::
            readCpa ::
            addListenerAction(MaxSparkListener(61, 70)) ::
            readNotArrivalHosp ::
            addListenerAction(MaxSparkListener(71, 80)) ::
            readGyc ::
            splitMktJobsMap.getOrElse(mkt, throw new Exception(s"undefined market=$mkt")) ::
            addListenerAction(MaxSparkListener(81, 90)) ::
            phPfizerPanelCommonAction(df) ::
            phSavePanelJob(df) ::
            addListenerAction(MaxSparkListener(91, 99)) ::
            phPanelInfo2Redis(df) ::
            Nil
    }
}
