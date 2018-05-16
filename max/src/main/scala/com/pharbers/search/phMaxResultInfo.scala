package com.pharbers.search

import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-5-14.
  */
case class phMaxResultInfo(user: String, company: String, ym:String, mkt: String) extends phMaxSearchTrait {

    private val rd = new PhRedisDriver()
    private val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)
    private val max_sales_city_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "max_sales_city_lst_key")
    private val max_sales_prov_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "max_sales_prov_lst_key")
    private val company_sales_city_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "company_sales_city_lst_key")
    private val company_sales_prov_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "company_sales_prov_lst_key")

    val lastYearYM = getLastYearYM(ym)
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

    def getLastSeveralMonthResultSalesLst(severalCount: Int): List[Map[String, JsValue]] = getLastSeveralMonthYM(severalCount, ym).reverse.map(singleYM => {
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
        Map("date" -> toJson(singleYM), "percentage" -> toJson(tempPercentage), "marketSales" -> toJson(getFormatValue(tempMaxSales)))
    })

    def getCityLstMap: List[Map[String, JsValue]] = {
        val currCompanyCityLst = rd.getListAllValue(company_sales_city_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            Map("City" -> temp(0), "Sales" -> temp(1))
        })
        val lastYearCompanyCityLst = rd.getListAllValue(last_year_company_sales_prov_lst_key) match {
            case Nil => Nil
            case lst => lst.map({x =>
                val temp = x.replace("[","").replace("]","").split(",")
                Map("City" -> temp(0), "Sales" -> temp(1))
            })
        }
        val lastYearMaxCityLst = rd.getListAllValue(last_year_max_sales_city_lst_key) match {
            case Nil => Nil
            case lst => lst.map({x =>
                val temp = x.replace("[","").replace("]","").split(",")
                Map("City" -> temp(0), "Sales" -> temp(1))
            })
        }
        rd.getListAllValue(max_sales_city_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            val companyCityMap: Map[String, String] = currCompanyCityLst.find(x => x("City") == temp(0)).getOrElse(Map("Sales" -> "0"))
            val tempShare = companyCityMap("Sales").toString.toDouble/temp(1).toDouble
            val tempLastYearMaxSalesMap = lastYearMaxCityLst.find(x => x("City") == temp(0)).getOrElse(Map("Sales" -> "0"))
            val tempLastYearCompanySalesMap = lastYearCompanyCityLst.find(x => x("City") == temp(0)).getOrElse(Map("Sales" -> "0"))
            val tempLastYearShare: Double = if (tempLastYearMaxSalesMap("Sales").toDouble == 0.0) 0.0 else tempLastYearCompanySalesMap("Sales").toDouble / tempLastYearMaxSalesMap("Sales").toDouble
            Map(
                "City" -> toJson(temp(0)),
                "CompanySales" -> toJson(getFormatValue(companyCityMap("Sales").toDouble)),
                "TotalSales" -> toJson(getFormatValue(temp(1).toDouble)),
                "Share" -> toJson(tempShare),
                "lastYearYMCompanySales" -> toJson(getFormatValue(tempLastYearCompanySalesMap("Sales").toDouble)),
                "lastYearYMTotalSales" -> toJson(getFormatValue(tempLastYearMaxSalesMap("Sales").toDouble)),
                "lastYearYMShare" -> toJson(tempLastYearShare)
            )
        })
    }

    def getProvLstMap: List[Map[String, JsValue]] = {
        val currCompanyProvLst = rd.getListAllValue(company_sales_prov_lst_key).map({x =>
            val temp = x.replace("[","").replace("]","").split(",")
            Map("Province" -> temp(0), "Sales" -> temp(1))
        })
        val lastYearCompanyProvLst = rd.getListAllValue(last_year_company_sales_prov_lst_key) match {
            case Nil => Nil
            case lst => lst.map({x =>
                val temp = x.replace("[","").replace("]","").split(",")
                Map("Province" -> temp(0), "Sales" -> temp(1))
            })
        }
        val lastYearMaxProvLst = rd.getListAllValue(last_year_max_sales_prov_lst_key) match {
            case Nil => Nil
            case lst => lst.map({x =>
                val temp = x.replace("[","").replace("]","").split(",")
                Map("Province" -> temp(0), "Sales" -> temp(1))
            })
        }

        rd.getListAllValue(max_sales_prov_lst_key).map{ x =>
            val temp = x.replace("[","").replace("]","").split(",")
            val companyProvMap = currCompanyProvLst.find(x => x("Province") == temp(0)).getOrElse(Map("Sales" -> "0"))
            val tempShare: Double = companyProvMap("Sales").toDouble/temp(1).toDouble
            val tempLastYearMaxSalesMap = lastYearMaxProvLst.find(x => x("Province") == temp(0)).getOrElse(Map("Sales" -> "0"))
            val tempLastYearCompanySalesMap = lastYearCompanyProvLst.find(x => x("Province") == temp(0)).getOrElse(Map("Sales" -> "0"))
            val tempLastYearShare = if (tempLastYearMaxSalesMap("Sales").toDouble == 0.0) 0.0
                else tempLastYearCompanySalesMap("Sales").toDouble / tempLastYearMaxSalesMap("Sales").toDouble

            Map(
                "Province" -> toJson(temp(0)),
                "CompanySales" -> toJson(getFormatValue(companyProvMap("Sales").toDouble)),
                "TotalSales" -> toJson(getFormatValue(temp(1).toDouble)),
                "Share" -> toJson(tempShare),
                "lastYearYMCompanySales" -> toJson(getFormatValue(tempLastYearCompanySalesMap("Sales").toDouble)),
                "lastYearYMTotalSales" -> toJson(getFormatValue(tempLastYearMaxSalesMap("Sales").toDouble)),
                "lastYearYMShare" -> toJson(tempLastYearShare)
            )
        }
    }

}
