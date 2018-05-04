package com.pharbers.unitTest.builder

class NhwaParamterBuilder[PassedStep <: NhwaBuildStep] private(
                                                               var company: String,
                                                               var data: String,
                                                               var market: String,
                                                               var cpa_file: String,
                                                              var resultMatch_file: String
                                                             ){
  protected def this() = this("", "", "", "", "")

  protected def this(pb: NhwaParamterBuilder[_]) = this(
    pb.company,
    pb.data,
    pb.market,
    pb.cpa_file,
    pb.resultMatch_file
  )

  def setCompany(company: String): NhwaParamterBuilder[NhwaCompanyStep] = {
    this.company = company
    new NhwaParamterBuilder[NhwaCompanyStep](this)
  }

  def setData(data: String): NhwaParamterBuilder[NhwaDataStep] = {
    this.data = data
    new NhwaParamterBuilder[NhwaDataStep](this)
  }
  
  def setMarket(market: String): NhwaParamterBuilder[NhwaMarketStep] = {
    this.market = market
    new NhwaParamterBuilder[NhwaMarketStep](this)
  }
  
  def setCpa_file(cpa_file: String)(implicit ev: PassedStep =:= NhwaMarketStep): NhwaParamterBuilder[NhwaCpa_fileStep] = {
    this.cpa_file = cpa_file
    new NhwaParamterBuilder[NhwaCpa_fileStep](this)
  }
  
  def setResultMatch_file(resultMatch_file: String): NhwaParamterBuilder[PassedStep] = {
    this.resultMatch_file = resultMatch_file
    this
  }

  def build()(implicit ev: PassedStep =:= NhwaCpa_fileStep):NhwaParamter = NhwaParamter(company, data, market, cpa_file, resultMatch_file)
}

object NhwaParamterBuilder {
  def apply() = new NhwaParamterBuilder[NhwaBuildStep]()
}
