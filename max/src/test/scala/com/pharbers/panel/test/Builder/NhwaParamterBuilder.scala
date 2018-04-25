package com.pharbers.panel.test.Builder

class NhwaParamterBuilder[PassedStep <: BuildStep] private(
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

  def setCompany(company: String): NhwaParamterBuilder[HasCompanyStep] = {
    this.company = company
    new NhwaParamterBuilder[HasCompanyStep](this)
  }

  def setData(data: String): NhwaParamterBuilder[HasDataStep] = {
    this.data = data
    new NhwaParamterBuilder[HasDataStep](this)
  }

  def setMarket(market: String)(implicit ev: PassedStep =:= HasDataStep): NhwaParamterBuilder[HasMarketStep] = {
    this.market = market
    new NhwaParamterBuilder[HasMarketStep](this)
  }

  def setCpa_file(cpa_file: String): NhwaParamterBuilder[PassedStep] = {
    this.cpa_file = cpa_file
    this
  }

  def build()(implicit ev: PassedStep =:= HasMarketStep):NhwaParamter = NhwaParamter(company, data, market, cpa_file)
}

object NhwaParamterBuilder {
  def apply() = new NhwaParamterBuilder[BuildStep]()
}
