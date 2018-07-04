package com.pharbers.delivery.pfizer

import java.util.UUID

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.excel.input.PhExcelXLSXCommonFormat
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.generalactions.memory.phMemoryArgs
import com.pharbers.pactions.jobs.{choiceJob, sequenceJob, sequenceJobWithMap}

/**
  * Created by jeorch on 18-6-5.
  */
class phPfizerDeliveryJob(args: Map[String, String]) extends sequenceJobWithMap {
    override val name: String = "export_delivery_data_action"
    override val defaultArgs: pActionArgs = NULLArgs

    lazy val company: String = args("company")
    val temp_name: String = UUID.randomUUID().toString
    val temp_dir: String = max_path_obj.p_cachePath + temp_name + "/"
    val match_dir: String = max_path_obj.p_matchFilePath

    val universe_file: String = match_dir + args("universe_file_delivery")
    val bridge_cpa_ims_ljx_file: String = match_dir + args("bridge_cpa_ims_ljx_file")
    val city_match_file: String = match_dir + args("city_match_file")

    implicit val companyArgs: phMemoryArgs = phMemoryArgs(company)

    /**
      * 1. read pfizer universe file
      */
    val loadUniverseFile: choiceJob = new choiceJob {
        override val name = "universe_file"
        val actions: List[pActionTrait] = existenceRdd("universe_file") ::
            csv2DFAction(temp_dir + "universe_file") ::
            new sequenceJob {
                override val name: String = "read_universe_file_job"
                override val actions: List[pActionTrait] =
                    xlsxReadingAction[phPfizerDeliveryUniverseFormat](universe_file, "universe_file") ::
                        saveCurrenResultAction(temp_dir + "universe_file") ::
                        csv2DFAction(temp_dir + "universe_file") :: Nil
            } :: Nil
    }

    /**
      * 2. read pfizer bridge file
      */
    val loadBridgeFile: choiceJob = new choiceJob {
        override val name = "bridge_file"
        val actions: List[pActionTrait] = existenceRdd("bridge_file") ::
            csv2DFAction(temp_dir + "bridge_file") ::
            new sequenceJob {
                override val name: String = "read_bridge_file_job"
                override val actions: List[pActionTrait] =
                    xlsxReadingAction[PhExcelXLSXCommonFormat](bridge_cpa_ims_ljx_file, "bridge_file") ::
                        saveCurrenResultAction(temp_dir + "bridge_file") ::
                        csv2DFAction(temp_dir + "bridge_file") :: Nil
            } :: Nil
    }

    /**
      * 3. read pfizer city match file
      */
    val loadCityMatchFile: choiceJob = new choiceJob {
        override val name = "city_match_file"
        val actions: List[pActionTrait] = existenceRdd("city_match_file") ::
            csv2DFAction(temp_dir + "city_match_file") ::
            new sequenceJob {
                override val name: String = "read_city_match_job"
                override val actions: List[pActionTrait] =
                    xlsxReadingAction[PhExcelXLSXCommonFormat](city_match_file, "city_match_file") ::
                        saveCurrenResultAction(temp_dir + "city_match_file") ::
                        csv2DFAction(temp_dir + "city_match_file") :: Nil
            } :: Nil
    }

    override val actions: List[pActionTrait] =
        loadUniverseFile ::
        loadBridgeFile ::
        loadCityMatchFile ::
        phPfizerDeliveryAction(args) ::
        Nil
}
