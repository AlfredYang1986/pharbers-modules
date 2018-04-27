package com.pharbers.panel.test

import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.astellas.phAstellasPanelJob
import com.pharbers.panel.nhwa.phNhwaPanelJob
import com.pharbers.panel.test.Builder.{AsttlasParamter, CompanyParamter, NhwaParamter}

class ExecuteJob(list: List[CompanyParamter]) {
  def getresultPath: List[String] ={
    val resultlst: List[String] = list.map(x => x match {
      case nhwaparamter: NhwaParamter => executeJob(nhwaparamter)
      case asttlasParamter: AsttlasParamter => executeJob(asttlasParamter)
    })
    resultlst
  }
  def executeJob(nhwaParamter:NhwaParamter): String ={
    val result = phNhwaPanelJob(nhwaParamter.cpa_file, nhwaParamter.data, nhwaParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
    result.toString
  }
  def executeJob(asttlasParamter: AsttlasParamter): String ={
    val result = phAstellasPanelJob(asttlasParamter.cpa_file, asttlasParamter.gycx_file, asttlasParamter.data, asttlasParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
    result.toString
  }
}

object ExecuteJob{
  def apply(list: List[CompanyParamter]): ExecuteJob = new ExecuteJob(list)
}
