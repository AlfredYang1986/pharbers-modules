package com.pharbers.unitTest.builder

case class CompanyParamterFactory() {
    
    def nhwaParamterBuild(company: String, data: String, market: String, cpa_file: String, resultMatch_file: String): NhwaParamter = {
        val nhwaParamenter = NhwaParamterBuilder()
                .setCompany(company)
                .setData(data)
                .setMarket(market)
                .setCpa_file(cpa_file)
                .setResultMatch_file(resultMatch_file)
                .build()
        nhwaParamenter
    }
    
    def asttlasParamterBuild(company: String, data: String, market: String, cpa_file: String, gycx_file: String, resultMatch_file: String): AsttlasParamter = {
        val assttlasParamenter = AsttlasParamterBuilder()
                .setCompany(company)
                .setData(data)
                .setMarket(market)
                .setCpa_file(cpa_file)
                .setGycx_file(gycx_file)
                .setResultMatch_file(resultMatch_file)
                .build()
        assttlasParamenter
    }
}