package com.pharbers.panel.test

class nhwaJobParameterBuilder() extends JobParameterBuilder {
  private val jobParameter = new JobParameter
  val parameter = readXmlFile.getXmlParameter

  override def buildCompany(): Unit = jobParameter.company = parameter("company")

  override def buildData(): Unit = jobParameter.data = parameter("data")

  override def buildInputFile(): Unit = jobParameter.inputFile = parameter("inputFile")

  override def buildMarket(): Unit = jobParameter.market = parameter("market")

  override def creatjobParameter(): JobParameter = jobParameter
}
