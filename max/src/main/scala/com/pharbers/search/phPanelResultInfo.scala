package com.pharbers.search

import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity

/**
  * Created by jeorch on 18-5-14.
  */
case class phPanelResultInfo(user: String, company: String, ym:String, mkt: String) extends phMaxSearchTrait {

    private val rd = new PhRedisDriver()
    private val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)
    //TODO:SinglePanelSpecialKey for example -> not_panel_hosp_key it depends on (user + company + ym + mkt) but had same key
    private val not_panel_hosp_key = Sercurity.md5Hash(user + company + ym + mkt + "not_panel_hosp_lst")

    val lastYearYM = getLastYearYM(ym)
    val lastYearSingleJobKey = Sercurity.md5Hash(user + company + lastYearYM + mkt)



    def getPanelName = rd.getMapValue(singleJobKey, "panel_name")
    def getHospCount = rd.getMapValue(singleJobKey, "panel_hosp_count").toInt
    def getProdCount = rd.getMapValue(singleJobKey, "panel_prod_count").toInt
    def getPanelSales = rd.getMapValue(singleJobKey, "panel_sales").toDouble

    def getLastYearHospCount = rd.getMapValue(lastYearSingleJobKey, "panel_hosp_count") match {
        case null => 0
        case sale => sale.toInt
    }
    def getLastYearProdCount = rd.getMapValue(lastYearSingleJobKey, "panel_prod_count") match {
        case null => 0
        case sale => sale.toInt
    }
    def getLastYearPanelSales = rd.getMapValue(lastYearSingleJobKey, "panel_sales") match {
        case null => 0
        case sale => sale.toInt
    }

    def getNotPanelHospLst = rd.getListAllValue(not_panel_hosp_key)

    def getCurrCompanySales = rd.getMapValue(singleJobKey, "panel_company_sales").toDouble
    def getCurrCompanyShare = getCurrCompanySales/getPanelSales

}
