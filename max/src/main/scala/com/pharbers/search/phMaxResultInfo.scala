package com.pharbers.search

import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity

/**
  * Created by jeorch on 18-5-14.
  */
case class phMaxResultInfo(user: String, company: String, ym:String, mkt: String) {

    private val rd = new PhRedisDriver()
    private val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)
    private val max_sales_city_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "max_sales_city_lst_key")
    private val max_sales_prov_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "max_sales_prov_lst_key")
    private val company_sales_city_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "company_sales_city_lst_key")
    private val company_sales_prov_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "company_sales_prov_lst_key")

    def getMaxResultSales = rd.getMapValue(singleJobKey, "max_sales").toDouble
    def getCurrCompanySales = rd.getMapValue(singleJobKey, "max_company_sales").toDouble

    def getCityLstMap = {
        val currCompanyCityLst = rd.getListAllValue(company_sales_city_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            Map("City" -> temp(0), "Sales" -> temp(1))
        })
        rd.getListAllValue(max_sales_city_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            val companyCityMap = currCompanyCityLst.find(x => x("City") == temp(0)).getOrElse(Map("Sales" -> 0))
            val tempShare = companyCityMap("Sales").toString.toDouble/temp(1).toDouble
            Map("City" -> temp(0), "CompanySales" -> companyCityMap("Sales"), "TotalSales" -> temp(1), "Share" -> tempShare)
        })
    }

    def getProvLstMap = {
        val currCompanyProvLst = rd.getListAllValue(company_sales_prov_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            Map("Province" -> temp(0), "Sales" -> temp(1))
        })
        rd.getListAllValue(max_sales_prov_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            val companyProvMap = currCompanyProvLst.find(x => x("Province") == temp(0)).getOrElse(Map("Sales" -> 0))
            val tempShare = companyProvMap("Sales").toString.toDouble/temp(1).toDouble
            Map("Province" -> temp(0), "CompanySales" -> companyProvMap("Sales"), "TotalSales" -> temp(1), "Share" -> tempShare)
        })
    }

}
