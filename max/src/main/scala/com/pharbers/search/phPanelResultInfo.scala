package com.pharbers.search

import java.util.Base64

import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import com.pharbers.sercuity.Sercurity
import com.pharbers.driver.PhRedisDriver
import com.mongodb.casbah.Imports.DBObject
import play.api.libs.json.{JsString, JsValue}
import com.pharbers.builder.phMarketTable.MongoDBPool._

/**
  * Created by jeorch on 18-5-14.
  */
case class phPanelResultInfo(user: String, company: String, ym:String, mkt: String) extends phMaxSearchTrait {

    private val rd = new PhRedisDriver()
    private val singleJobKey = Base64.getEncoder.encodeToString((company +"#"+ ym +"#"+ mkt).getBytes())
    private val not_panel_hosp_key = Sercurity.md5Hash(user + company + ym + mkt + "not_panel_hosp_lst")

    val lastYearYM: String = getLastYearYM(ym)
    val lastYearSingleJobKey: String = Sercurity.md5Hash(user + company + lastYearYM + mkt)

    def getHospCount: Int = rd.getMapValue(singleJobKey, "panel_hosp_count").toInt
    def getProdCount: Int = rd.getMapValue(singleJobKey, "panel_prod_count").toInt
    def getPanelSales: Double = rd.getMapValue(singleJobKey, "panel_sales").toDouble

    val baseLine: Map[String, List[String]] = {
        val db = MongoPool.queryDBInstance("market").get

        val query: DBObject = {
            DBObject("Company" -> company)
            DBObject("Market" -> mkt)
        }

        val output: DBObject => Map[String, JsValue] = { obj =>
            Map(
                "Date" -> toJson(obj.as[Int]("Date").toString),
                "Sales" -> toJson(getFormatSales(obj.as[Double]("Sales")).toString),
                "HOSP_ID" -> toJson(obj.as[Double]("HOSP_ID").toInt.toString),
                "Prod_Name" -> toJson(obj.as[Double]("Prod_Name").toInt.toString)
            )
        }


        val tmp = db.queryMultipleObject(query, "BaseLine", "Date")(output).reverse
        val baselineResult = if(tmp.size == 12){
            tmp
        } else {
            val defaultData = for(i <- 1 to 12) yield {
                Map(
                    "Sales" -> toJson("0"),
                    "HOSP_ID" -> toJson("0"),
                    "Prod_Name" -> toJson("0")
                )
            }
            defaultData.toList
        }

        Map(
            "Sales" -> baselineResult.map(_("Sales").as[JsString].value),
            "HOSP_ID" -> baselineResult.map(_("HOSP_ID").as[JsString].value),
            "Prod_Name" -> baselineResult.map(_("Prod_Name").as[JsString].value)
        )
    }

    def getLastYearHospCount(month: Int): String = baseLine("HOSP_ID")(month - 1)
    def getLastYearProdCount(month: Int): String = baseLine("Prod_Name")(month - 1)
    def getLastYearPanelSales(month: Int): String = baseLine("Sales")(month - 1)

    def getNotPanelHospLst: List[String] = rd.getSetAllValue(not_panel_hosp_key).toList

    def getCurrCompanySales: Double = rd.getMapValue(singleJobKey, "panel_company_sales").toDouble
    def getCurrCompanyShare: Double = getCurrCompanySales/getPanelSales

    def setValue2Array(index: Int, value: String): Array[String] = {
        val a = Array.fill(12)("0")
        a(index) = value
        a
    }

}
