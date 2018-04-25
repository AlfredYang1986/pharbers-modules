package com.pharbers.panel.test.Builder

class CompanyParamterFactory {
  def NhwaParamterBuild(paramterMap: Map[String, String]): NhwaParamter ={
    val nhwaParamenter = NhwaParamterBuilder()
      .setCompany(paramterMap("company"))
      .setData(paramterMap("data"))
      .setMarket(paramterMap("market"))
      .setCpa_file(paramterMap("cpa_file"))
      .build()
    nhwaParamenter
  }
}

object CompanyParamterFactory{
  def apply: CompanyParamterFactory = new CompanyParamterFactory()
}
