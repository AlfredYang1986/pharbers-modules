package com.pharbers.unitTest.builder

case class PfizerParamter(
                                  company: String,
                                  data: String,
                                  market: String,
                                  cpa_file: String,
                                  gycx_file: String,
                                  resultMatch_file: String
                          ) extends CompanyParamter {
    require(company != "", "不可缺少 company 参数")
    require(data != "", "不可缺少 data 参数")
    require(market != "", "不可缺少 market 参数")
    require(cpa_file != "", "不可缺少 cpa_file 参数")
    require(gycx_file != "", "不可缺少 gycx_file 参数")
    require(resultMatch_file != "", "不可缺少 resultMatch_file 参数")
    override def getCompany: String = company
}

sealed trait PfizerBuildStep

sealed trait PfizerCompanyStep extends PfizerBuildStep

sealed trait PfizerDataStep extends PfizerBuildStep

sealed trait PfizerMarketStep extends PfizerBuildStep

sealed trait PfizerCpa_fileStep extends PfizerBuildStep

sealed trait PfizerGycx_fileStep extends PfizerBuildStep