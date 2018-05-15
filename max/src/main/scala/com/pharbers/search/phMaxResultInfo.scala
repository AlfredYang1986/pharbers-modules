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

    def getLastYearYM = (ym.take(4).toInt - 1) + ym.takeRight(2)
    val lastYearSingleJobKey = Sercurity.md5Hash(user + company + getLastYearYM + mkt)





//    val lastMonthSingleJobKey = Sercurity.md5Hash(user + company + getLastMonthYM + mkt)

    def getLastYearResultSales =rd.getMapValue(lastYearSingleJobKey, "max_sales") match {
        case null => 0.toDouble
        case sale => sale.toDouble
    }

    def getLastYearCurrCompanySales =rd.getMapValue(lastYearSingleJobKey, "max_company_sales") match {
        case null => 0.toDouble
        case sale => sale.toDouble
    }

//    def getLastMonthResultSales =rd.getMapValue(lastMonthSingleJobKey, "max_sales") match {
//        case null => 0.toDouble
//        case sale => sale.toDouble
//    }
//
//    def getLastMonthCurrCompanySales =rd.getMapValue(lastMonthSingleJobKey, "max_company_sales") match {
//        case null => 0.toDouble
//        case sale => sale.toDouble
//    }

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

    def getLastMonthYM(yearMonth: String) = yearMonth.takeRight(2) match {
        case "01" => (yearMonth.take(4).toInt - 1) + "12"
        case month => if (month.toInt<10) yearMonth.take(5) + (yearMonth.takeRight(1).toInt - 1) else yearMonth.take(4) + (yearMonth.takeRight(2).toInt - 1)
    }

    //TODO:获取12个月的ym
    def getLastTwelveMonthYM(yearMonth: String) = {
        var tempYM = yearMonth
        (1 to 12).map(x => {
            tempYM = getLastMonthYM(tempYM)
        })
    }

}
