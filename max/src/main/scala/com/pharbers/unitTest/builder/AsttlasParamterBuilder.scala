package com.pharbers.unitTest.builder

class AsttlasParamterBuilder[PassedStep <: AsttlasBuildStep] private(
                                                               var company: String,
                                                               var data: String,
                                                               var market: String,
                                                               var cpa_file: String,
                                                               var gycx_file: String,
                                                               var resultMatch_file: String
                                                             ){
  protected def this() = this("", "", "", "", "", "")

  protected def this(pb: AsttlasParamterBuilder[_]) = this(
    pb.company,
    pb.data,
    pb.market,
    pb.cpa_file,
    pb.gycx_file,
    pb.resultMatch_file
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
  
  def setCpa_file(cpa_file: String): AsttlasParamterBuilder[AsttlasCpa_fileStep] = {
    this.cpa_file = cpa_file
    new AsttlasParamterBuilder[AsttlasCpa_fileStep](this)
  }
  
  def setGycx_file(gycx_file: String)(implicit ev: PassedStep =:= AsttlasCpa_fileStep): AsttlasParamterBuilder[AsttlasGycx_fileStep] = {
    this.gycx_file = gycx_file
    new AsttlasParamterBuilder[AsttlasGycx_fileStep](this)
  }
  
  def setResultMatch_file(resultMatch_file: String): AsttlasParamterBuilder[PassedStep] = {
    this.resultMatch_file = resultMatch_file
    this
  }
  
  def build()(implicit ev: PassedStep =:= AsttlasGycx_fileStep):AsttlasParamter = AsttlasParamter(company, data, market, cpa_file, gycx_file, resultMatch_file)
}

object AsttlasParamterBuilder{
  def apply() = new AsttlasParamterBuilder[AsttlasBuildStep]()
}
