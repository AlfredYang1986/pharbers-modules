package com.pharbers.unitTest.builder

case class NhwaParamter(
                               company: String,
                               data: String,
                               market: String,
                               cpa_file: String,
                               resultMatch_file: String
                       ) extends CompanyParamter {
    require(company != "", "不可缺少 company 参数")
    require(data != "", "不可缺少 data 参数")
    require(market != "", "不可缺少 market 参数")
    require(cpa_file != "", "不可缺少 cpa_file 参数")
    require(resultMatch_file != "", "不可缺少 resultMatch_file 参数")
    override def getCompany: String = company
}

sealed trait NhwaBuildStep

sealed trait NhwaCompanyStep extends NhwaBuildStep

sealed trait NhwaDataStep extends NhwaBuildStep

sealed trait NhwaMarketStep extends NhwaBuildStep

sealed trait NhwaCpa_fileStep extends NhwaBuildStep