package com.pharbers.panel.nhwa

import java.util.UUID

import akka.actor.Actor
import com.pharbers.pactions.jobs._
import play.api.libs.json.Json.toJson
import com.pharbers.panel.nhwa.format._
import com.pharbers.channel.sendEmTrait
import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.generalactions._
import com.pharbers.panel.common.phSavePanelJob
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.generalactions.memory.phMemoryArgs
import com.pharbers.common.excel.input.{PhExcelXLSXCommonFormat, PhXlsxThirdSheetFormat}
import org.apache.spark
import org.apache.spark.{MaxSparkListener, addListenerAction}

object phNhwaPanelJob {

    def apply(arg_cpa: String, arg_ym: String, arg_mkt: String)(implicit _actor: Actor): phNhwaPanelJob = {
        new phNhwaPanelJob {
            override lazy val not_published_hosp_file: String = match_dir + "nhwa/2017年未出版医院名单.xlsx"
            override lazy val universe_file: String = match_dir + "nhwa/universe_麻醉市场_online.xlsx"
            override lazy val product_match_file: String = match_dir + "nhwa/nhwa匹配表.xlsx"
            override lazy val fill_hos_data_file: String = match_dir + "nhwa/补充医院.xlsx"
            override lazy val markets_match_file: String = match_dir + "nhwa/通用名市场定义.xlsx"
            override lazy val cpa_file: String = arg_cpa

            override lazy val ym: String = arg_ym
            override lazy val mkt: String = arg_mkt
            override lazy val uid: String = "testUser"
            override lazy val actor: Actor = _actor
        }
    }
}

/**
* 1. read 2017年未出版医院名单.xlsx
* 2. read universe_麻醉市场_online.xlsx
* 3. read 匹配表
* 4. read 补充医院
* 5. read 通用名市场定义, 读第三页
* 6. read CPA文件第一页
* 7. read CPA文件第二页
**/
trait phNhwaPanelJob extends sequenceJobWithMap {
    override val name: String = "phNhwaPanelJob"
    implicit val companyArgs: phMemoryArgs = phMemoryArgs("Nhwa")

    val temp_name: String = UUID.randomUUID().toString
    val temp_dir: String = max_path_obj.p_cachePath + temp_name + "/"
    val match_dir: String = max_path_obj.p_matchFilePath

    val not_published_hosp_file: String
    val universe_file: String
    val product_match_file: String
    val fill_hos_data_file: String
    val markets_match_file: String
    val cpa_file: String

    val ym: String
    val mkt: String
    val uid: String

    /**
      * 1. read 未出版医院文件
      */
    val loadNotPublishedHosp: choiceJob = new choiceJob {
        override val name = "not_published_hosp_file"
        val actions: List[pActionTrait] = existenceRdd("not_published_hosp_file") ::
                csv2DFAction(temp_dir + "/not_published_hosp_file") ::
                new sequenceJob {
                    override val name: String = "read_not_published_hosp_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[PhExcelXLSXCommonFormat](not_published_hosp_file, "not_published_hosp_file") ::
                                saveCurrenResultAction(temp_dir + "not_published_hosp_file") ::
                                csv2DFAction(temp_dir + "not_published_hosp_file") :: Nil
                } :: Nil
    }

    /**
      * 2. read universe file
      */
    val loadUniverseFile: choiceJob = new choiceJob {
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

    /**
      * 3. read product match file
      */
    val loadProductMatchFile: choiceJob = new choiceJob {
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

    /**
      * 4. read full hosp file
      */
    val loadFullHospFile: choiceJob = new choiceJob {
        override val name = "full_hosp_file"
        val actions: List[pActionTrait] = existenceRdd("full_hosp_file") ::
                csv2DFAction(temp_dir + "full_hosp_file") ::
                new sequenceJob {
                    override val name: String = "read_full_hosp_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[phNhwaCpaFormat](fill_hos_data_file, "full_hosp_file") ::
                                saveCurrenResultAction(temp_dir + "full_hosp_file") ::
                                csv2DFAction(temp_dir + "full_hosp_file") :: Nil
                } :: Nil
    }

    /**
      * 5. read market match file
      */
    val loadMarketMatchFile: choiceJob = new choiceJob {
        override val name = "markets_match_file"
        val actions: List[pActionTrait] = existenceRdd("markets_match_file") ::
                csv2DFAction(temp_dir + "markets_match_file") ::
                new sequenceJob {
                    override val name: String = "read_markets_match_file_job"
                    override val actions: List[pActionTrait] =
                        xlsxReadingAction[PhXlsxThirdSheetFormat](markets_match_file, "markets_match_file") ::
                                saveCurrenResultAction(temp_dir + "markets_match_file") ::
                                csv2DFAction(temp_dir + "markets_match_file") :: Nil
                } :: Nil
    }

    /**
      * 6. read CPA文件第一页
      */
    val readCpa: sequenceJob = new sequenceJob {
        override val name = "cpa"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                    saveCurrenResultAction(temp_dir + "cpa") ::
                    csv2DFAction(temp_dir + "cpa") :: Nil
    }

    /**
      * 7. read CPA文件第二页
      */
    val readNotArrivalHosp: sequenceJob = new sequenceJob {
        override val name = "not_arrival_hosp_file"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phNhwaCpaSecondSheetFormat](cpa_file, "not_arrival_hosp_file") ::
                    saveCurrenResultAction(temp_dir + "not_arrival_hosp_file") ::
                    csv2DFAction(temp_dir + "not_arrival_hosp_file") :: Nil
    }

    val df = MapArgs(
        Map(
            "ym" -> StringArgs(ym),
            "mkt" -> StringArgs(mkt),
            "name" -> StringArgs(temp_name)
        )
    )



    implicit val actor: Actor
    implicit val sendProgress: (sendEmTrait, Double) => Unit = { (em, progress) =>
        em.sendMessage(uid, "panel", "ing", toJson(Map("progress" -> toJson(progress))))
    }

    override val actions: List[pActionTrait] = { jarPreloadAction() ::
                setLogLevelAction("ERROR") ::
                spark.addListenerAction(MaxSparkListener(0, 5)) ::
                loadNotPublishedHosp ::
                spark.addListenerAction(MaxSparkListener(6, 10)) ::
                loadUniverseFile ::
                spark.addListenerAction(MaxSparkListener(11, 15)) ::
                loadProductMatchFile ::
                addListenerAction(MaxSparkListener(16, 20)) ::
                loadFullHospFile ::
                spark.addListenerAction(MaxSparkListener(21, 25)) ::
                loadMarketMatchFile ::
                addListenerAction(MaxSparkListener(26, 30)) ::
                readCpa ::
                readNotArrivalHosp ::
                addListenerAction(MaxSparkListener(31, 90)) ::
                phNhwaPanelConcretJob(df) ::
                phSavePanelJob(df) ::
                Nil
    }

}