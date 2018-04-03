package com.pharbers.panel2.common

import com.pharbers.panel2.astellas.phAstellasHandle
import com.pharbers.panel2.nhwa.phNhwaHandle

/**
  * Created by spark on 18-4-3.
  */
trait phPanelInstance extends phPanelTrait

case class phMarket(market_name: String){ def get = this.market_name }

case class phPanelFactory(company: String) {

    val companyMap = Map(
        "8ee0ca24796f9b7f284d931650edbd4b" -> "Nhwa",
        "235f39e3da85c2dee2a2b20d004a8b77" -> "Astellas"
    )

    val marketLst = Map(
        "Nhwa" -> List("麻醉市场") ,
        "Astellas" -> List("Allelock")
    )

    def getInstance: phPanelInstance = {
        companyMap(company) match {
            case "Nhwa" => phNhwaHandle()
            case "Astellas" => phAstellasHandle()
            case _ => ???
        }
    }

    def getInstance(market: phMarket): phPanelInstance = {
        (companyMap(company), market.get) match {
            case ("Nhwa", "麻醉市场") => phNhwaHandle()
            case ("Astellas", _) => phAstellasHandle()
            case _ => ???
        }
    }

    def getMarkets: List[phMarket] = {
        companyMap.get(company) match {
            case Some(mkt) => marketLst.get(mkt) match {
                case Some(lst) => lst.map(phMarket)
                case _ => ???
            }
            case _ => ???
        }
    }

}
