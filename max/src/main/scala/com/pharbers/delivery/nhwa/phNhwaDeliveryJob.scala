package com.pharbers.delivery.nhwa

import java.util.UUID

import com.pharbers.common.algorithm.{max_path_obj, phSparkCommonFuncTrait}
import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.excel.input.PhExcelXLSXCommonFormat
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.generalactions.memory.phMemoryArgs
import com.pharbers.pactions.jobs.{choiceJob, sequenceJob, sequenceJobWithMap}

/**
  * Created by jeorch on 18-6-5.
  */
class phNhwaDeliveryJob(args: Map[String, String]) extends sequenceJobWithMap with phSparkCommonFuncTrait {
    override val name: String = "export_delivery_data_action"
    override val defaultArgs: pActionArgs = NULLArgs

    lazy val company: String = args("company")
    val temp_name: String = UUID.randomUUID().toString
    val temp_dir: String = max_path_obj.p_cachePath + temp_name + "/"
    val match_dir: String = max_path_obj.p_matchFilePath

    val product_match_file: String = match_dir + args("product_match_file")
    val hospital_match_file: String = match_dir + args("hospital_match_file")
    val acc_match_file: String = match_dir + args("acc_match_file")
    val area_match_file: String = match_dir + args("area_match_file")

    implicit val companyArgs: phMemoryArgs = phMemoryArgs(company)

    /**
      * 1. read nhwa product match file
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
      * 2. read nhwa hospital match file
      */
    val loadHospitalMatchFile: choiceJob = new choiceJob {
        override val name = "hospital_match_file"
        val actions: List[pActionTrait] = existenceRdd("hospital_match_file") ::
            csv2DFAction(temp_dir + "hospital_match_file") ::
            new sequenceJob {
                override val name: String = "read_hospital_match_job"
                override val actions: List[pActionTrait] =
                    xlsxReadingAction[PhExcelXLSXCommonFormat](hospital_match_file, "hospital_match_file") ::
                        saveCurrenResultAction(temp_dir + "hospital_match_file") ::
                        csv2DFAction(temp_dir + "hospital_match_file") :: Nil
            } :: Nil
    }

    /**
      * 3. read nhwa acc match file
      */
    val loadAccMatchFile: choiceJob = new choiceJob {
        override val name = "acc_match_file"
        val actions: List[pActionTrait] = existenceRdd("acc_match_file") ::
            csv2DFAction(temp_dir + "acc_match_file") ::
            new sequenceJob {
                override val name: String = "read_acc_match_job"
                override val actions: List[pActionTrait] =
                    xlsxReadingAction[PhExcelXLSXCommonFormat](acc_match_file, "acc_match_file") ::
                        saveCurrenResultAction(temp_dir + "acc_match_file") ::
                        csv2DFAction(temp_dir + "acc_match_file") :: Nil
            } :: Nil
    }

    /**
      * 4. read nhwa area match file
      */
    val loadAreaMatchFile: choiceJob = new choiceJob {
        override val name = "area_match_file"
        val actions: List[pActionTrait] = existenceRdd("area_match_file") ::
            csv2DFAction(temp_dir + "area_match_file") ::
            new sequenceJob {
                override val name: String = "read_area_match_job"
                override val actions: List[pActionTrait] =
                    xlsxReadingAction[PhExcelXLSXCommonFormat](area_match_file, "area_match_file") ::
                        saveCurrenResultAction(temp_dir + "area_match_file") ::
                        csv2DFAction(temp_dir + "area_match_file") :: Nil
            } :: Nil
    }

    override val actions: List[pActionTrait] =
        loadProductMatchFile ::
        loadHospitalMatchFile ::
        loadAccMatchFile ::
        loadAreaMatchFile ::
        phNhwaDeliveryAction(args) :: Nil
}
