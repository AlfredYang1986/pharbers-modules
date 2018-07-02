package com.pharbers.panel.astellas

import java.util.UUID

import akka.actor.Actor
import com.pharbers.channel.util.sendEmTrait
import org.apache.spark.listener
import com.pharbers.pactions.jobs._
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.astellas.format._
import com.pharbers.pactions.generalactions._
import com.pharbers.common.algorithm.max_path_obj
import org.apache.spark.listener.addListenerAction
import org.apache.spark.listener.progress.sendMultiProgress
import com.pharbers.panel.common.{phPanelInfo2Redis, phSavePanelJob}
import com.pharbers.pactions.excel.input.PhExcelXLSXCommonFormat
import com.pharbers.pactions.generalactions.memory.phMemoryArgs

case class phAstellasPanelJob(args: Map[String, String])(implicit _actor: Actor) extends sequenceJobWithMap {
    override val name: String = "phAstellasPanelJob"

    val temp_name: String = UUID.randomUUID().toString
    val temp_dir: String = max_path_obj.p_cachePath + temp_name + "/"
    val match_dir: String = max_path_obj.p_matchFilePath
    val source_dir: String = max_path_obj.p_clientPath

    val universe_file: String = match_dir + args("universe_file")
    val product_match_file: String = match_dir + args("product_match_file")
    val markets_match_file: String = match_dir + args("markets_match_file")
    val hospital_file: String = match_dir + args("hospital_file")
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


    //1. read 产品匹配表
    val load_product_match_file: choiceJob = new choiceJob {
        override val name = "product_match_file"
        val actions: List[pActionTrait] = existenceRdd("product_match_file") ::
                csv2DFAction(temp_dir + "product_match_file") ::
                new sequenceJob {
                    override val name: String = "read_product_match_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[phAstellasProductMatchFormat](product_match_file, "product_match_file") ::
                                saveCurrenResultAction(temp_dir + "product_match_file") ::
                                csv2DFAction(temp_dir + "product_match_file") :: Nil
                } :: Nil
    }

    //2. read 市场匹配表
    val load_markets_match_file: choiceJob = new choiceJob {
        override val name = "markets_match_file"
        val actions: List[pActionTrait] = existenceRdd("markets_match_file") ::
                csv2DFAction(temp_dir + "markets_match_file") ::
                new sequenceJob {
                    override val name: String = "read_markets_match_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[phAstellasMarketsMatchFormat](markets_match_file, "markets_match_file") ::
                                saveCurrenResultAction(temp_dir + "markets_match_file") ::
                                csv2DFAction(temp_dir + "markets_match_file") :: Nil
                } :: Nil
    }

    //3. read universe_file文件
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

    //4. read hospital_file文件
    val load_hospital_file: choiceJob = new choiceJob {
        override val name = "hospital_file"
        val actions: List[pActionTrait] = existenceRdd("hospital_file") ::
                csv2DFAction(temp_dir + "hospital_file") ::
                new sequenceJob {
                    override val name: String = "read_hospital_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[phAstellasHospitalFormat](hospital_file, "hospital_file") ::
                                saveCurrenResultAction(temp_dir + "hospital_file") ::
                                csv2DFAction(temp_dir + "hospital_file") :: Nil
                } :: Nil
    }

    //5. read CPA源文件
    val load_cpa: sequenceJob = new sequenceJob {
        override val name = "cpa"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phAstellasCpaFormat](cpa_file, "cpa") ::
                    saveCurrenResultAction(temp_dir + "cpa") ::
                    csv2DFAction(temp_dir + "cpa") :: Nil
    }

    //6. read GYC源文件
    val load_gycx: sequenceJob = new sequenceJob {
        override val name = "gycx"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phAstellasGycxFormat](gyc_file, "gycx") ::
                    saveCurrenResultAction(temp_dir + "gycx") ::
                    csv2DFAction(temp_dir + "gycx") :: Nil
    }

    lazy val df = MapArgs(
        Map(
            "ym" -> StringArgs(ym),
            "mkt" -> StringArgs(mkt),
            "mkt_en" -> StringArgs(getMktEN(mkt)),
            "user" -> StringArgs(user),
            "name" -> StringArgs(temp_name),
            "company" -> StringArgs(company),
            "job_id" -> StringArgs(job_id)
        )
    )

    def getMktEN(mkt: String): String = {
        mkt match {
            case "阿洛刻市场" => "Allelock"
            case "米开民市场" => "Mycamine"
            case "普乐可复市场" => "Prograf"
            case "佩尔市场" => "Perdipine"
            case "哈乐市场" => "Harnal"
            case "痛风市场" => "Gout"
            case "卫喜康市场" => "Vesicare"
            case "Grafalon市场" => "Grafalon"
        }
    }

    override val actions: List[pActionTrait] = { jarPreloadAction() ::
            setLogLevelAction("ERROR") ::
            addListenerAction(listener.MaxSparkListener(0, 10, "load_product_match_file")) ::
            load_product_match_file ::
            addListenerAction(listener.MaxSparkListener(11, 20, "load_markets_match_file")) ::
            load_markets_match_file ::
            addListenerAction(listener.MaxSparkListener(21, 30, "load_universe_file")) ::
            load_universe_file ::
            addListenerAction(listener.MaxSparkListener(31, 40, "load_hospital_file")) ::
            load_hospital_file ::
            addListenerAction(listener.MaxSparkListener(41, 50, "load_cpa")) ::
            load_cpa ::
            addListenerAction(listener.MaxSparkListener(51, 60, "load_gycx")) ::
            load_gycx ::
            addListenerAction(listener.MaxSparkListener(61, 90, "phAstellasPanelConcretJob")) ::
            phAstellasPanelConcretJob(df) ::
            phSavePanelJob(df) ::
            addListenerAction(listener.MaxSparkListener(91, 99)) ::
            phPanelInfo2Redis(df) ::
            Nil
    }
}