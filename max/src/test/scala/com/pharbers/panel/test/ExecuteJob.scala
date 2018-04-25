package com.pharbers.panel.test

import com.pharbers.panel.nhwa.phNhwaPanelJob
import com.pharbers.panel.test.Builder.{CompanyParamter, NhwaParamter}

class ExecuteJob {
  def this(nhwaParamter: NhwaParamter) {
    this()
    phNhwaPanelJob(nhwaParamter.cpa_file, nhwaParamter.data, nhwaParamter.market).perform()
  }
}

object ExecuteJob{
  def apply[T <:CompanyParamter](): ExecuteJob = new ExecuteJob()
}
