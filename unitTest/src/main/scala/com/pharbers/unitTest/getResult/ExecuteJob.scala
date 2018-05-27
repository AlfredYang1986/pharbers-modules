//package com.pharbers.unitTest.getResult
//
//import com.pharbers.calc.phMaxJob
//import com.pharbers.pactions.actionbase.MapArgs
//import com.pharbers.panel.astellas.phAstellasPanelJob
//import com.pharbers.panel.nhwa.phNhwaPanelJob
//import com.pharbers.panel.pfizer.phPfizerPanelJob
//import com.pharbers.unitTest.builder._
//
//class ExecuteJob(companyParamter: CompanyParamter) {
//    def getresultPath: Map[String, String] ={
//        val result = companyParamter match {
//            case nhwaParamter: NhwaParamter => executeJob(nhwaParamter)
//            case asttlasParamter: AsttlasParamter => executeJob(asttlasParamter)
//            case pfizerParamter: PfizerParamter => executeJob(pfizerParamter)
//        }
//        result
//    }
//
//    def executeJob(nhwaParamter:NhwaParamter): Map[String, String] ={
//        val panelResult = phNhwaPanelJob(nhwaParamter.cpa_file, nhwaParamter.data, nhwaParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//        val result = phMaxJob(panelResult, "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
//        Map("maxResult" -> result , "resultMatch_file" -> nhwaParamter.resultMatch_file)
//    }
//
//    def executeJob(asttlasParamter: AsttlasParamter): Map[String, String] ={
//        val panelResult = phAstellasPanelJob(asttlasParamter.cpa_file, asttlasParamter.gycx_file, asttlasParamter.data, asttlasParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//        val result = phMaxJob(panelResult, s"astellas/UNIVERSE_${asttlasParamter.market}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
//        Map("maxResult" -> result , "resultMatch_file" -> asttlasParamter.resultMatch_file)
//    }
//
//    def executeJob(pfizerParamter: PfizerParamter): Map[String, String] ={
//        val panelResult = phPfizerPanelJob(pfizerParamter.cpa_file, pfizerParamter.gycx_file, pfizerParamter.data, pfizerParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//        println("panelResult=" + panelResult)
//        val result = phMaxJob(panelResult, s"pfizer/universe_${pfizerParamter.market}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
//        println("MaxResult=" + result)
//        Map("maxResult" -> result , "resultMatch_file" -> pfizerParamter.resultMatch_file)
//    }
//}
//
//object ExecuteJob{
//    def apply(companyParamter: CompanyParamter): ExecuteJob = new ExecuteJob(companyParamter)
//}
