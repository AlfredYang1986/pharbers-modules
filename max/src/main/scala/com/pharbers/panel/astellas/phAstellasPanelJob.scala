package com.pharbers.panel.astellas

import java.util.UUID

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.jobs._
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.astellas.format._
import com.pharbers.pactions.generalactions._
import com.pharbers.panel.common.phSavePanelJob
import com.pharbers.common.excel.input.PhExcelXLSXCommonFormat
import com.pharbers.pactions.generalactions.memory.phMemoryArgs

object phAstellasPanelJob {

    def apply(arg_cpa: String, arg_gyc: String, arg_ym: String, arg_mkt: String) : phAstellasPanelJob = {
        new phAstellasPanelJob {
            override lazy val cpa_file: String = arg_cpa
            override lazy val gyc_file: String = arg_gyc
            override lazy val ym: String = arg_ym
            override lazy val mkt: String = arg_mkt

            override lazy val temp_name: String = UUID.randomUUID().toString
        }
    }
}


trait phAstellasPanelJob extends sequenceJobWithMap {
    override val name: String = "phAstellasPanelJob"
    implicit val companyArgs: phMemoryArgs = phMemoryArgs("Astellas")

    val ym: String
    val mkt: String
    val cpa_file: String
    val gyc_file: String
    val temp_name: String

    val mkt_cn: String = getMktCN(mkt)
    val match_dir: String = max_path_obj.p_matchFilePath
    val temp_dir: String = max_path_obj.p_cachePath + temp_name + "/"
    val product_match_file: String = match_dir + "astellas/20171018药品最小单位IMS packid匹配表.xlsx"
    val markets_match_file: String = match_dir + "astellas/20170203药品名称匹配市场.xlsx"
    val universe_file: String = match_dir + s"astellas/UNIVERSE_${mkt}_online.xlsx"
    val hospital_file: String = match_dir + "astellas/医院名称编码等级三源互匹20180314.xlsx"

    //1. read 产品匹配表
    val load_product_match_file = new choiceJob {
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
    val load_markets_match_file = new choiceJob {
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
    val load_universe_file = new choiceJob {
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
    val load_hospital_file = new choiceJob {
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
    val load_cpa = new sequenceJob {
        override val name = "cpa"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phAstellasCpaFormat](cpa_file, "cpa") ::
                    saveCurrenResultAction(temp_dir + "cpa") ::
                    csv2DFAction(temp_dir + "cpa") :: Nil
    }

    //6. read GYC源文件
    val load_gycx = new sequenceJob {
        override val name = "gycx"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phAstellasGycxFormat](gyc_file, "gycx") ::
                    saveCurrenResultAction(temp_dir + "gycx") ::
                    csv2DFAction(temp_dir + "gycx") :: Nil
    }

    val df = MapArgs(
        Map(
            "ym" -> StringArgs(ym),
            "mkt" -> StringArgs(mkt),
            "mkt_cn" -> StringArgs(mkt_cn),
            "name" -> StringArgs(temp_name)
        )
    )

    def getMktCN(mkt: String): String = {
        mkt match {
            case "Allelock" => "阿洛刻市场"
            case "Mycamine" => "米开民市场"
            case "Prograf" => "普乐可复市场"
            case "Perdipine" => "佩尔市场"
            case "Harnal" => "哈乐市场"
            case "Gout" => "痛风市场"
            case "Vesicare" => "卫喜康市场"
            case "Grafalon" => "Grafalon市场"
        }
    }

    override val actions: List[pActionTrait] = jarPreloadAction() ::
            load_product_match_file ::
            load_markets_match_file ::
            load_universe_file ::
            load_hospital_file ::
            load_cpa ::
            load_gycx ::
            phAstellasPanelConcretJob(df) ::
            phSavePanelJob(df) ::
            Nil
}