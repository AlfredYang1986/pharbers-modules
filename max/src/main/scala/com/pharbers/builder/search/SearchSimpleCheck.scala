package com.pharbers.builder.search

import com.pharbers.search.phPanelResultInfo
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait SearchSimpleCheck {
    def searchSimpleCheck(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val market = (jv \ "condition" \ "market").asOpt[String].get
        val ym = (jv \ "condition" \ "years").asOpt[String].get
        val month = ym.takeRight(2).toInt
        val user_id = (jv \ "user" \ "user_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get

        val panelInfo = phPanelResultInfo(user_id, company_id, ym, market)
        import panelInfo._

        val temp = Some(
            Map(
                "hospital" -> toJson(
                    Map(
                        "baselines" -> toJson(baseLine("HOSP_ID")),
                        "samplenumbers" -> toJson(setValue2Array(month - 1, getHospCount.toString)),
                        "currentNumber" -> toJson(getHospCount),
                        "lastYearNumber" -> toJson(getLastYearHospCount(month))
                    )
                ),
                "product" -> toJson(
                    Map(
                        "baselines" -> toJson(baseLine("Prod_Name")),
                        "samplenumbers" -> toJson(setValue2Array(month - 1, getProdCount.toString)),
                        "currentNumber" -> toJson(getProdCount),
                        "lastYearNumber" -> toJson(getLastYearProdCount(month))
                    )
                ),
                "sales" -> toJson(
                    Map(
                        "baselines" -> toJson(baseLine("Sales")),
                        "samplenumbers" -> toJson(setValue2Array(month - 1, getFormatSales(getPanelSales).toString)),
                        "currentNumber" -> toJson(getFormatSales(getPanelSales)),
                        "lastYearNumber" -> toJson(getLastYearPanelSales(month))
                    )
                ),
                "notfindhospital" -> toJson(panelInfo.getNotPanelHospLst.zipWithIndex.map(x => {
                    val temp = x._1.replace("[", "").replace("]", "").split(",")
                    toJson(Map(
                        "index" -> toJson(x._2 + 1),
                        "hospitalName" -> toJson(temp(0)),
                        "province" -> toJson(temp(1)),
                        "city" -> toJson(temp(2)),
                        "cityLevel" -> toJson(temp(3))
                    ))
                }))
            )
        )

        (temp, None)
    }
}
