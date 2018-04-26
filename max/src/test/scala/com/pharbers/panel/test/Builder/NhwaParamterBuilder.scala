package com.pharbers.panel.test.Builder

class NhwaParamterBuilder[PassedStep <: NhwaBuildStep] private(
                                                               var company: String,
                                                               var data: String,
                                                               var market: String,
                                                               var cpa_file: String
                                                             ){
  protected def this() = this("", "", "", "")

  protected def this(pb: NhwaParamterBuilder[_]) = this(
    pb.company,
    pb.data,
    pb.market,
    pb.cpa_file
  )

  def setCompany(company: String): NhwaParamterBuilder[NhwaCompanyStep] = {
    this.company = company
    new NhwaParamterBuilder[NhwaCompanyStep](this)
  }

  def setData(data: String): NhwaParamterBuilder[NhwaDataStep] = {
    this.data = data
    new NhwaParamterBuilder[NhwaDataStep](this)
  }

  def setMarket(market: String)(implicit ev: PassedStep =:= NhwaDataStep): NhwaParamterBuilder[NhwaMarketStep] = {
    this.market = market
    new NhwaParamterBuilder[NhwaMarketStep](this)
  }

  def setCpa_file(cpa_file: String): NhwaParamterBuilder[PassedStep] = {
    this.cpa_file = cpa_file
    this
  }

  def build()(implicit ev: PassedStep =:= NhwaMarketStep):NhwaParamter = NhwaParamter(company, data, market, cpa_file)
}

object NhwaParamterBuilder {
  def apply() = new NhwaParamterBuilder[NhwaBuildStep]()
}
