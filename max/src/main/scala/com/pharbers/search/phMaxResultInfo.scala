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

    val lastYearYM = (ym.take(4).toInt - 1) + ym.takeRight(2)
    val lastYearSingleJobKey = Sercurity.md5Hash(user + company + lastYearYM + mkt)
    val last_year_max_sales_city_lst_key = Sercurity.md5Hash(user + company + lastYearYM + mkt + "max_sales_city_lst_key")
    val last_year_max_sales_prov_lst_key = Sercurity.md5Hash(user + company + lastYearYM + mkt + "max_sales_prov_lst_key")
    val last_year_company_sales_city_lst_key = Sercurity.md5Hash(user + company + lastYearYM + mkt + "company_sales_city_lst_key")
    val last_year_company_sales_prov_lst_key = Sercurity.md5Hash(user + company + lastYearYM + mkt + "company_sales_prov_lst_key")

    def getMaxResultSales = rd.getMapValue(singleJobKey, "max_sales").toDouble
    def getLastYearResultSales = rd.getMapValue(lastYearSingleJobKey, "max_sales") match {
        case null => 0.toDouble
        case sale => sale.toDouble
    }
    def getLastYearResultSalesPercentage = {
        val lastYearResultSales = getLastYearResultSales
        lastYearResultSales match {
            case 0.0 => 0.0
            case _ => (getMaxResultSales - lastYearResultSales)/lastYearResultSales
        }
    }

    def getCurrCompanySales = rd.getMapValue(singleJobKey, "max_company_sales").toDouble
    def getLastYearCurrCompanySales = rd.getMapValue(lastYearSingleJobKey, "max_company_sales") match {
        case null => 0.toDouble
        case sale => sale.toDouble
    }
    def getLastYearCurrCompanySalesPercentage = {
        val lastYearCompanySales = getLastYearCurrCompanySales
        lastYearCompanySales match {
            case 0.0 => 0.0
            case _ => (getCurrCompanySales - lastYearCompanySales)/lastYearCompanySales
        }
    }

    def getLastSeveralMonthResultSalesLst(severalCount: Int) = getLastSeveralMonthYM(severalCount, ym).map(singleYM => {
        val tempSingleJobKey = Sercurity.md5Hash(user + company + singleYM + mkt)
        val tempMaxSales = rd.getMapValue(tempSingleJobKey, "max_sales") match {
            case null => 0.toDouble
            case sale => sale.toDouble
        }
        val tempCompanySales = rd.getMapValue(tempSingleJobKey, "max_company_sales") match {
            case null => 0.toDouble
            case sale => sale.toDouble
        }
        val tempPercentage = tempMaxSales match {
            case 0.0 => 0.0
            case _ => tempCompanySales/tempMaxSales
        }
        Map("date" -> singleYM, "percentage" -> tempPercentage, "marketSales" -> tempMaxSales)
    })

    def getCityLstMap = {
        val currCompanyCityLst = rd.getListAllValue(company_sales_city_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            Map("City" -> temp(0), "Sales" -> temp(1))
        })
        val lastYearCompanyCityLst = rd.getListAllValue(last_year_company_sales_prov_lst_key) match {
            case List.empty => List.empty
            case lst => lst.map({x =>
                val temp = x.replace("[","").replace("]","").split(",")
                Map("City" -> temp(0), "Sales" -> temp(1))
            })
        }
        val lastYearMaxCityLst = rd.getListAllValue(last_year_max_sales_city_lst_key) match {
            case List.empty => List.empty
            case lst => lst.map({x =>
                val temp = x.replace("[","").replace("]","").split(",")
                Map("City" -> temp(0), "Sales" -> temp(1))
            })
        }
        rd.getListAllValue(max_sales_city_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            val companyCityMap = currCompanyCityLst.find(x => x("City") == temp(0)).getOrElse(Map("Sales" -> 0))
            val tempShare = companyCityMap("Sales").toString.toDouble/temp(1).toDouble
            val tempLastYearMaxSalesMap = lastYearMaxCityLst.find(x => x("City") == temp(0)).getOrElse(Map("Sales" -> 0))
            val tempLastYearCompanySalesMap = lastYearCompanyCityLst.find(x => x("City") == temp(0)).getOrElse(Map("Sales" -> 0))
            val tempLastYearShare = if (tempLastYearMaxSalesMap("Sales").toString.toDouble == 0.0) 0.0 else (tempLastYearCompanySalesMap("Sales").toString.toDouble/tempLastYearMaxSalesMap("Sales").toString.toDouble)
            Map(
                "City" -> temp(0),
                "CompanySales" -> companyCityMap("Sales"),
                "TotalSales" -> temp(1),
                "Share" -> tempShare,
                "lastYearYMCompanySales" -> tempLastYearCompanySalesMap("Sales"),
                "lastYearYMTotalSales" -> tempLastYearMaxSalesMap("Sales"),
                "lastYearYMShare" -> tempLastYearShare
            )
        })
    }

    def getProvLstMap = {
        val currCompanyProvLst = rd.getListAllValue(company_sales_prov_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            Map("Province" -> temp(0), "Sales" -> temp(1))
        })
        val lastYearCompanyProvLst = rd.getListAllValue(last_year_company_sales_prov_lst_key) match {
            case List.empty => List.empty
            case lst => lst.map({x =>
                val temp = x.replace("[","").replace("]","").split(",")
                Map("Province" -> temp(0), "Sales" -> temp(1))
            })
        }
        val lastYearMaxProvLst = rd.getListAllValue(last_year_max_sales_prov_lst_key) match {
            case List.empty => List.empty
            case lst => lst.map({x =>
                val temp = x.replace("[","").replace("]","").split(",")
                Map("Province" -> temp(0), "Sales" -> temp(1))
            })
        }
        rd.getListAllValue(max_sales_prov_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            val companyProvMap = currCompanyProvLst.find(x => x("Province") == temp(0)).getOrElse(Map("Sales" -> 0))
            val tempShare = companyProvMap("Sales").toString.toDouble/temp(1).toDouble
            val tempLastYearMaxSalesMap = lastYearMaxProvLst.find(x => x("Province") == temp(0)).getOrElse(Map("Sales" -> 0))
            val tempLastYearCompanySalesMap = lastYearCompanyProvLst.find(x => x("Province") == temp(0)).getOrElse(Map("Sales" -> 0))
            val tempLastYearShare = if (tempLastYearMaxSalesMap("Sales").toString.toDouble == 0.0) 0.0 else (tempLastYearCompanySalesMap("Sales").toString.toDouble/tempLastYearMaxSalesMap("Sales").toString.toDouble)
            Map(
                "Province" -> temp(0),
                "CompanySales" -> companyProvMap("Sales"),
                "TotalSales" -> temp(1),
                "Share" -> tempShare,
                "lastYearYMCompanySales" -> tempLastYearCompanySalesMap("Sales"),
                "lastYearYMTotalSales" -> tempLastYearMaxSalesMap("Sales"),
                "lastYearYMShare" -> tempLastYearShare
            )
        })
    }

    def getLastMonthYM(yearMonth: String) = yearMonth.takeRight(2) match {
        case "01" => (yearMonth.take(4).toInt - 1) + "12"
        case month => if (month.toInt == 10) yearMonth.take(4) + "09" else  yearMonth.take(5) + (yearMonth.takeRight(1).toInt - 1)
    }

    def getLastSeveralMonthYM(severalCount: Int, yearMonth: String) = {
        var tempYM = yearMonth
        val lst = (1 to severalCount - 1).map(x => {
            tempYM = getLastMonthYM(tempYM)
            tempYM
        }).toList
        yearMonth :: lst
    }

}
