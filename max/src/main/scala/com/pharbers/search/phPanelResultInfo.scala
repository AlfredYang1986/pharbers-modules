package com.pharbers.search

import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity

/**
  * Created by jeorch on 18-5-14.
  */
case class phPanelResultInfo(user: String, company: String, ym:String, mkt: String) {

    private val rd = new PhRedisDriver()
    private val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)
    //TODO:SinglePanelSpecialKey for example -> not_panel_hosp_key it depends on (user + company + ym + mkt) but had same key
    private val not_panel_hosp_key = Sercurity.md5Hash(user + company + ym + mkt + "not_panel_hosp_lst")

    def getPanelName = rd.getMapValue(singleJobKey, "panel_name")
    def getHospCount = rd.getMapValue(singleJobKey, "panel_hosp_count").toInt
    def getProdCount = rd.getMapValue(singleJobKey, "panel_prod_count").toInt
    def getPanelSales = rd.getMapValue(singleJobKey, "panel_sales").toDouble
    def getPanelCurrCompanySales = rd.getMapValue(singleJobKey, "panel_company_sales").toDouble
    def getNotPanelHospLst = rd.getListAllValue(not_panel_hosp_key)

}
