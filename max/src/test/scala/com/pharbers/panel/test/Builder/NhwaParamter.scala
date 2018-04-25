package com.pharbers.panel.test.Builder

case class NhwaParamter (
                            company: String,
                            data: String,
                            market: String,
                            cpa_file: String
                          )extends CompanyParamter {
  require(company != "", "不可缺少 company 参数")
  require(data != "", "不可缺少 data 参数")
  require(market != "", "不可缺少 market 参数")
  require(cpa_file != "", "不可缺少 cpa_file 参数")
}
sealed trait BuildStep
sealed trait HasCompanyStep extends BuildStep
sealed trait HasDataStep extends BuildStep
sealed trait HasMarketStep extends BuildStep
