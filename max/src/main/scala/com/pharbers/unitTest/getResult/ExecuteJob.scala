package com.pharbers.unitTest.getResult

import com.pharbers.calc.phMaxJob
import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.astellas.phAstellasPanelJob
import com.pharbers.panel.nhwa.phNhwaPanelJob
import com.pharbers.unitTest.builder._

class ExecuteJob(companyParamter: CompanyParamter) {
    def getresultPath: Map[String, String] ={
        val result = companyParamter match {
            case nhwaParamter: NhwaParamter => executeJob(nhwaParamter)
            case asttlasParamter: AsttlasParamter => executeJob(asttlasParamter)
        }
        result
    }
    
    def executeJob(nhwaParamter:NhwaParamter): Map[String, String] ={
        val panelResult = phNhwaPanelJob(nhwaParamter.cpa_file, nhwaParamter.data, nhwaParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
        val result = phMaxJob(panelResult, "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
        Map("maxResult" -> result , "resultMatch_file" -> nhwaParamter.resultMatch_file)
    }
    
    def executeJob(asttlasParamter: AsttlasParamter): Map[String, String] ={
        val panelResult = phAstellasPanelJob(asttlasParamter.cpa_file, asttlasParamter.gycx_file, asttlasParamter.data, asttlasParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
        val result = phMaxJob(panelResult, s"astellas/UNIVERSE_${asttlasParamter.market}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
        Map("maxResult" -> result , "resultMatch_file" -> asttlasParamter.resultMatch_file)
    }
}

object ExecuteJob{
    def apply(companyParamter: CompanyParamter): ExecuteJob = new ExecuteJob(companyParamter)
}
