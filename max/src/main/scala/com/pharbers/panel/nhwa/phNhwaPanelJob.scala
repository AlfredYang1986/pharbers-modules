package com.pharbers.panel.nhwa

import java.util.UUID
import com.pharbers.pactions.jobs._
import com.pharbers.panel.nhwa.format._
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.panel_path_obj
import com.pharbers.pactions.generalactions._
import com.pharbers.panel.common.phSavePanelJob

object phNhwaPanelJob {

    def apply(arg_cpa : String, arg_ym: String, arg_mkt: String) : phNhwaPanelJob = {
        new phNhwaPanelJob {
            override lazy val cpa_file: String = arg_cpa
            override lazy val ym: String = arg_ym
            override lazy val mkt: String = arg_mkt

            override lazy val temp_name: String = UUID.randomUUID().toString
        }
    }
}

/**
  * 6. read CPA文件第一页
  * 7. read CPA文件第二页
  */
trait phNhwaPanelJob extends sequenceJobWithMap {
    override val name: String = "phNhwaPanelJob"

    val ym: String
    val mkt: String
    val cpa_file: String
    val temp_name: String
    val temp_dir: String = panel_path_obj.p_cachePath + temp_name + "/"

    /**
      * 6. read CPA文件第一页
      */
    val readCpa = new sequenceJob {
        override val name = "cpa"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                    saveCurrenResultAction(temp_dir + "cpa") ::
                    csv2DFAction(temp_dir + "cpa") :: Nil
    }

    /**
      * 7. read CPA文件第二页
      */
    val readNotArrivalHosp = new sequenceJob {
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

    override val actions: List[pActionTrait] =
        jarPreloadAction() ::
                phNhwaPreActions(temp_name).actions :::
                readCpa ::
                readNotArrivalHosp ::
                phNhwaPanelConcretJob(df) ::
                phSavePanelJob(df) :: Nil
}