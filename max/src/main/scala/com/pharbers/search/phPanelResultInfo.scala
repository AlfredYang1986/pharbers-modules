package com.pharbers.search

import com.mongodb.casbah.Imports.DBObject
import com.pharbers.sercuity.Sercurity
import com.pharbers.driver.PhRedisDriver
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsString, JsValue}
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-5-14.
  */
case class phPanelResultInfo(user: String, company: String, ym:String, mkt: String) extends phMaxSearchTrait {

    private val rd = new PhRedisDriver()
    private val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)
    private val not_panel_hosp_key = Sercurity.md5Hash(user + company + ym + mkt + "not_panel_hosp_lst")

    val lastYearYM = getLastYearYM(ym)
    val lastYearSingleJobKey = Sercurity.md5Hash(user + company + lastYearYM + mkt)

    def getHospCount: Int = rd.getMapValue(singleJobKey, "panel_hosp_count").toInt
    def getProdCount: Int = rd.getMapValue(singleJobKey, "panel_prod_count").toInt
    def getPanelSales: Double = rd.getMapValue(singleJobKey, "panel_sales").toDouble

    val baseLine: Map[String, List[String]] = {
        val db = new dbInstanceManager{}.queryDBInstance("calc").get

        val query: DBObject = {
            DBObject("Company" -> company)
            DBObject("Market" -> mkt)
        }

        val output: DBObject => Map[String, JsValue] = { obj =>
            Map(
                "Sales" -> toJson(getFormatSales(obj.as[Double]("Sales")).toString),
                "HOSP_ID" -> toJson(obj.as[Double]("HOSP_ID").toInt.toString),
                "Prod_Name" -> toJson(obj.as[Double]("Prod_Name").toInt.toString)
            )
        }


        val tmp = db.queryMultipleObject(query, "BaseLine", "Month")(output)
        // TODO: 对于没有基准线的公司或市场，先使用恩华的代替
        val baselineResult = if(tmp.size == 12){
            tmp
        } else {
            val query: DBObject = {
                DBObject("Company" -> "5afa53bded925c05c6f69c54")
                DBObject("Market" -> "麻醉市场")
            }
            db.queryMultipleObject(query, "BaseLine", "Month")(output)
        }

        Map(
            "Sales" -> baselineResult.map(_("Sales").as[JsString].value),
            "HOSP_ID" -> baselineResult.map(_("HOSP_ID").as[JsString].value),
            "Prod_Name" -> baselineResult.map(_("Prod_Name").as[JsString].value)
        )
    }

    def getLastYearHospCount(month: Int) = baseLine("HOSP_ID")(month - 1)
    def getLastYearProdCount(month: Int) = baseLine("Prod_Name")(month - 1)
    def getLastYearPanelSales(month: Int) = baseLine("Sales")(month - 1)

    def getNotPanelHospLst = rd.getSetAllValue(not_panel_hosp_key).toList

    def getCurrCompanySales = rd.getMapValue(singleJobKey, "panel_company_sales").toDouble
    def getCurrCompanyShare = getCurrCompanySales/getPanelSales

    def setValue2Array(index: Int, value: String): Array[String] = {
        val a = Array.fill(12)("0")
        a(index) = value
        a
    }

}
