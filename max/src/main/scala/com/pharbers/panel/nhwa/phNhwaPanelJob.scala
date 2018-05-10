package com.pharbers.panel.nhwa

import java.util.UUID

import akka.actor.Actor
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.common.excel.input.{PhExcelXLSXCommonFormat, PhXlsxThirdSheetFormat}
import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.generalactions.memory.phMemoryArgs
import com.pharbers.pactions.jobs._
import com.pharbers.panel.common.{phPanelId2Redis, phSavePanelJob}
import com.pharbers.panel.nhwa.format._
import org.apache.spark.listener
import org.apache.spark.listener.addListenerAction
import org.apache.spark.listener.progress.multiProgressTrait

object phNhwaPanelJob {

    def apply(_company: String, _user: String)
             (_ym: String, _mkt: String, _cpa: String, _p_current: Int, _p_total: Int)
             (_not_published_hosp_file: String,
              _universe_file: String,
              _product_match_file: String,
              _fill_hos_data_file: String,
              _markets_match_file: String)(implicit _actor: Actor): phNhwaPanelJob = {
        new phNhwaPanelJob {
            override lazy val not_published_hosp_file: String = match_dir + _not_published_hosp_file
            override lazy val universe_file: String = match_dir + _universe_file
            override lazy val product_match_file: String = match_dir + _product_match_file
            override lazy val fill_hos_data_file: String = match_dir + _fill_hos_data_file
            override lazy val markets_match_file: String = match_dir + _markets_match_file
            override lazy val cpa_file: String = _cpa

            override lazy val ym: String = _ym
            override lazy val mkt: String = _mkt
            override lazy val user: String = _user
            override lazy val company: String = _company
            override lazy val actor: Actor = _actor
            override lazy val p_total: Double = _p_total
            override lazy val p_current: Double = _p_current
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
trait phNhwaPanelJob extends sequenceJobWithMap with multiProgressTrait {
    override val name: String = "phNhwaPanelJob"

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
    implicit val companyArgs: phMemoryArgs = phMemoryArgs(company)

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
            "user" -> StringArgs(user),
            "name" -> StringArgs(temp_name),
            "company" -> StringArgs(company)
        )
    )

    override val actions: List[pActionTrait] = { jarPreloadAction() ::
                setLogLevelAction("ERROR") ::
                addListenerAction(listener.MaxSparkListener(0, 10)) ::
                loadNotPublishedHosp ::
                addListenerAction(listener.MaxSparkListener(11, 20)) ::
                loadUniverseFile ::
                addListenerAction(listener.MaxSparkListener(21, 30)) ::
                loadProductMatchFile ::
                addListenerAction(listener.MaxSparkListener(31, 40)) ::
                loadFullHospFile ::
                addListenerAction(listener.MaxSparkListener(41, 50)) ::
                loadMarketMatchFile ::
                addListenerAction(listener.MaxSparkListener(51, 60)) ::
                readCpa ::
                readNotArrivalHosp ::
                addListenerAction(listener.MaxSparkListener(61, 99)) ::
                phNhwaPanelConcretJob(df) ::
                phSavePanelJob(df) ::
                phPanelId2Redis(df) ::
                Nil
    }

}