package com.pharbers.calc

import java.util.UUID

import com.pharbers.pactions.jobs._
import com.pharbers.panel.panel_path_obj
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.common.excel.input.PhExcelXLSXCommonFormat

object phMaxJob {
    def apply(arg_panel_name: String) : phMaxJob = {
        new phMaxJob {
            override lazy val panel_name: String = arg_panel_name
            override lazy val universe_name: String = "nhwa/universe_麻醉市场_online.xlsx"
        }
    }
}


trait phMaxJob extends sequenceJobWithMap {
    override val name: String = "phMaxCalcJob"

    val panel_name: String
    val universe_name: String

    val panel_file: String = panel_path_obj.p_resultPath + panel_name
    val universe_file: String = panel_path_obj.p_matchFilePath + universe_name
    val temp_dir: String = panel_path_obj.p_cachePath + "/"
    val temp_universe_name: String = UUID.randomUUID().toString

    // 1. load panel data
    val loadPanelData = new sequenceJob {
        override val name: String = "panel_data"
        override val actions: List[pActionTrait] = csv2DFAction(panel_file) :: Nil
    }


    // 2. read universe file
    val readUniverseFile = new sequenceJob {
        override val name = "universe_data"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhExcelXLSXCommonFormat](universe_file, temp_universe_name) ::
                    saveCurrenResultAction(temp_dir + temp_universe_name) ::
                    csv2DFAction(temp_dir + temp_universe_name) :: Nil
    }


    override val actions: List[pActionTrait] = jarPreloadAction() ::
            loadPanelData ::
            readUniverseFile ::
            phMaxSplitAction() ::
            phMaxGroupAction() ::
            phMaxCalcActionq() ::
            phMaxBsonAction() :: Nil
}