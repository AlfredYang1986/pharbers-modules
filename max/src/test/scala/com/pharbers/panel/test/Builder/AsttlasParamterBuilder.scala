package com.pharbers.panel.test.Builder

class AsttlasParamterBuilder[PassedStep <: AsttlasBuildStep] private(
                                                               var company: String,
                                                               var data: String,
                                                               var market: String,
                                                               var cpa_file: String,
                                                               var gycx_file: String
                                                             ){
  protected def this() = this("", "", "", "", "")

  protected def this(pb: AsttlasParamterBuilder[_]) = this(
    pb.company,
    pb.data,
    pb.market,
    pb.cpa_file,
    pb.gycx_file
  )

  def setCompany(company: String): AsttlasParamterBuilder[AsttlasCompanyStep] = {
    this.company = company
    new AsttlasParamterBuilder[AsttlasCompanyStep](this)
  }

  def setData(data: String): AsttlasParamterBuilder[AsttlasDataStep] = {
    this.data = data
    new AsttlasParamterBuilder[AsttlasDataStep](this)
  }

  def setMarket(market: String): AsttlasParamterBuilder[AsttlasMarketStep] = {
    this.market = market
    new AsttlasParamterBuilder[AsttlasMarketStep](this)
  }

  def setCpa_file(cpa_file: String)(implicit ev: PassedStep =:= AsttlasMarketStep): AsttlasParamterBuilder[AsttlasCpa_fileStep] = {
    this.cpa_file = cpa_file
    new AsttlasParamterBuilder[AsttlasCpa_fileStep](this)
  }

  def setGycx_file(gycx_file: String): AsttlasParamterBuilder[PassedStep] = {
    this.gycx_file = gycx_file
    this
  }
  def build()(implicit ev: PassedStep =:= AsttlasCpa_fileStep):AsttlasParamter = AsttlasParamter(company, data, market, cpa_file, gycx_file)
}

object AsttlasParamterBuilder{
  def apply() = new AsttlasParamterBuilder[AsttlasBuildStep]()
}
