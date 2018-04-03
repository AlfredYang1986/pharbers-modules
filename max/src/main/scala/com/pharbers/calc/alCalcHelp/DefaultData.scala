package com.pharbers.calc.alCalcHelp

import com.pharbers.panel.panel_path_obj
import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.common.alFileHandler.fileConfig._
import com.pharbers.common.excel.event.alExcelDataParser
import com.pharbers.calc.alCalcHelp.alModel.{AdminHospitalDataBase, IntegratedData}


object DefaultData {
    object file_path extends PharbersInjectModule {
        override val id: String = "calc-path"
        override val configPath: String = "pharbers_config/calc_path.xml"
        override val md = "bson-path" :: "hosp" :: "field-names-hosp" :: "integrated" :: "field-names-integrated" :: Nil

        val hosp = config.mc.find(p => p._1 == "hosp").get._2.toString
        val field_names_hosp = config.mc.find(p => p._1 == "field-names-hosp").get._2.toString
        val integrated = config.mc.find(p => p._1 == "integrated").get._2.toString
        val field_names_integrated = config.mc.find(p => p._1 == "field-names-integrated").get._2.toString
    }

    def hospdatabase(path: String, company: String, market: String): List[AdminHospitalDataBase] = {
        val hospdata_ch_file = file_path.hosp
        val hospdata_en_file = file_path.field_names_hosp
        type target = AdminHospitalDataBase
        val hospdatabase = new alExcelDataParser(new target, hospdata_en_file, hospdata_ch_file)

        val mkt_file_local = panel_path_obj.p_client_path + company +
            panel_path_obj.p_universe_file.replace("##market##", market).replace(".csv", ".xlsx")
        hospdatabase.prase(mkt_file_local)("")
        hospdatabase.data.toList.asInstanceOf[List[target]]
    }

    val integratedxmlpath_ch = file_path.integrated
    val integratedxmlpath_en = file_path.field_names_integrated

    def integratedbase(filename: String, company: String): List[IntegratedData] = {
        type target = IntegratedData
        val integratedbase = new alExcelDataParser(new target, integratedxmlpath_en, integratedxmlpath_ch)
        integratedbase.prase(fileBase + company + outPut + filename)("")
        integratedbase.data.toList.asInstanceOf[List[target]]
    }
}
