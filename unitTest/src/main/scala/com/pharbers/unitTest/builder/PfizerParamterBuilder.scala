package com.pharbers.unitTest.builder

class PfizerParamterBuilder[PassedStep <: PfizerBuildStep] private(
                                                                            var company: String,
                                                                            var data: String,
                                                                            var market: String,
                                                                            var cpa_file: String,
                                                                            var gycx_file: String,
                                                                            var resultMatch_file: String
                                                                    ){
    protected def this() = this("", "", "", "", "", "")
    
    protected def this(pb: PfizerParamterBuilder[_]) = this(
        pb.company,
        pb.data,
        pb.market,
        pb.cpa_file,
        pb.gycx_file,
        pb.resultMatch_file
    )
    
    def setCompany(company: String): PfizerParamterBuilder[PfizerCompanyStep] = {
        this.company = company
        new PfizerParamterBuilder[PfizerCompanyStep](this)
    }
    
    def setData(data: String): PfizerParamterBuilder[PfizerDataStep] = {
        this.data = data
        new PfizerParamterBuilder[PfizerDataStep](this)
    }
    
    def setMarket(market: String): PfizerParamterBuilder[PfizerMarketStep] = {
        this.market = market
        new PfizerParamterBuilder[PfizerMarketStep](this)
    }
    
    def setCpa_file(cpa_file: String): PfizerParamterBuilder[PfizerCpa_fileStep] = {
        this.cpa_file = cpa_file
        new PfizerParamterBuilder[PfizerCpa_fileStep](this)
    }
    
    def setGycx_file(gycx_file: String)(implicit ev: PassedStep =:= PfizerCpa_fileStep): PfizerParamterBuilder[PfizerGycx_fileStep] = {
        this.gycx_file = gycx_file
        new PfizerParamterBuilder[PfizerGycx_fileStep](this)
    }
    
    def setResultMatch_file(resultMatch_file: String): PfizerParamterBuilder[PassedStep] = {
        this.resultMatch_file = resultMatch_file
        this
    }
    
    def build()(implicit ev: PassedStep =:= PfizerGycx_fileStep):PfizerParamter = PfizerParamter(company, data, market, cpa_file, gycx_file, resultMatch_file)
}

object PfizerParamterBuilder{
    def apply() = new PfizerParamterBuilder[PfizerBuildStep]()
}
