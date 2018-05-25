//package com.pharbers.panel.test
//
//import com.pharbers.calc.phMaxJob
//import com.pharbers.pactions.actionbase.MapArgs
//import com.pharbers.panel.astellas.phAstellasPanelJob
//import com.pharbers.panel.nhwa.phNhwaPanelJob
//import com.pharbers.panel.test.Builder.{AsttlasParamter, CompanyParamter, NhwaParamter}
//
//class ExecuteJob(list: List[CompanyParamter]) {
//  def getresultPath: List[String] ={
//      val resultlst: List[String] = list.map(x => x match {
//      case nhwaParamter: NhwaParamter => executeJob(nhwaParamter)
//      case asttlasParamter: AsttlasParamter => executeJob(asttlasParamter)
//    })
//    resultlst
//  }
//  def executeJob(nhwaParamter:NhwaParamter): String ={
//    val panelResult = phNhwaPanelJob(nhwaParamter.cpa_file, nhwaParamter.data, nhwaParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
////    val result = nhwaParamter.market match {
////      case "panel" => panelResult
////      case "max" => phMaxJob(panelResult, "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
////    }
//    val result = phMaxJob(panelResult, "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
//    result
//  }
//  def executeJob(asttlasParamter: AsttlasParamter): String ={
//    val panelResult = phAstellasPanelJob(asttlasParamter.cpa_file, asttlasParamter.gycx_file, asttlasParamter.data, asttlasParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
//    val result = phMaxJob(panelResult, s"astellas/UNIVERSE_${asttlasParamter.market}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get.toString
//    result
//  }
//}
//
//object ExecuteJob{
//  def apply(list: List[CompanyParamter]): ExecuteJob = new ExecuteJob(list)
//}
