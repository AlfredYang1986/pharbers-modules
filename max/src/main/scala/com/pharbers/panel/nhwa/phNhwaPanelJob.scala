package com.pharbers.panel.nhwa

import com.pharbers.pactions.jobs._
import com.pharbers.panel.panel_path_obj
import com.pharbers.common.excel.input._
import com.pharbers.pactions.actionbase._
import com.pharbers.pactions.generalactions._
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat

object phNhwaPanelJob {

    def apply(arg_cpa : String, arg_dest: String) : phNhwaPanelJob = {
        new phNhwaPanelJob {
            override lazy val cpa_file: String = arg_cpa
            override lazy val result_dir: String = arg_dest
        }
    }
}

/**
  * 6. read CPA文件第一页
  * 7. read CPA文件第二页
  */
trait phNhwaPanelJob extends sequenceJobWithMap {
    override val name: String = "phNhwaPanelJob"

    val cpa_file: String
    val mid_dir : String = panel_path_obj.p_cachePath + "nhwa/"
    val result_dir: String

    /**
      * 6. read CPA文件第一页
      */
    val readCpa = new sequenceJob {
        override val name = "cpa"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                    saveCurrenResultAction(mid_dir + "cpa") ::
                    csv2RddAction(mid_dir + "cpa") :: Nil
    }

    /**
      * 7. read CPA文件第二页
      */
    val readNotArrivalHosp = new sequenceJob {
        override val name = "not_arrival_hosp_file"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhXlsxSecondSheetFormat](cpa_file, "not_arrival_hosp_file") ::
                    saveCurrenResultAction(mid_dir + "not_arrival_hosp_file") ::
                    csv2RddAction(mid_dir + "not_arrival_hosp_file") :: Nil
    }

    val df = MapArgs(
        Map(
            "ym" -> StringArgs("201710"),
            "mkt" -> StringArgs("麻醉市场")
        )
    )

    override val actions: List[pActionTrait] = jarPreloadAction() ::
            PhNhwaPreActions.actions :::
            readCpa :: readNotArrivalHosp ::
            phNhwaPanelConcretJob(df) :: Nil
}