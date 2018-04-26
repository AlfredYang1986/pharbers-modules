package com.pharbers.panel.test

import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.astellas.phAstellasPanelJob
import com.pharbers.panel.nhwa.phNhwaPanelJob
import com.pharbers.panel.test.Builder.{AsttlasParamter, CompanyParamter, NhwaParamter}

class ExecuteJob() {
  def this(nhwaParamter: NhwaParamter) {
    this()
    phNhwaPanelJob(nhwaParamter.cpa_file, nhwaParamter.data, nhwaParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
  }
  def this(asttlasParamter: AsttlasParamter){
    this()
    phAstellasPanelJob(asttlasParamter.cpa_file, asttlasParamter.gycx_file, asttlasParamter.data, asttlasParamter.market).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
  }
}

object ExecuteJob{
  def apply[companyParamter <:CompanyParamter](): ExecuteJob = new ExecuteJob()
}
