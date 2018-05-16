package com.pharbers.builder.search

import com.pharbers.search.phPanelResultInfo
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

trait SearchSimpleCheck {
    def searchSimpleCheck(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val market = (jv \ "condition" \ "market").asOpt[String].get
        val ym = (jv \ "condition" \ "years").asOpt[String].get
        val user_id = (jv \ "user" \ "user_id").asOpt[String].get
        val company_id = (jv \ "user" \ "company" \ "company_id").asOpt[String].get

        val panelInfo = phPanelResultInfo(user_id, company_id, ym, market)

        val temp = Some(
            Map(
                "hospital" -> toJson(
                    Map(
                        "baselines" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "samplenumbers" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "currentNumber" -> toJson(panelInfo.getHospCount),
                        "lastYearNumber" -> toJson(panelInfo.getLastYearHospCount)
                    )
                ),
                "product" -> toJson(
                    Map(
                        "baselines" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "samplenumbers" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "currentNumber" -> toJson(panelInfo.getProdCount),
                        "lastYearNumber" -> toJson(panelInfo.getLastYearProdCount)
                    )
                ),
                "sales" -> toJson(
                    Map(
                        "baselines" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "samplenumbers" -> toJson("100" :: "200" :: "300" :: "400" :: "500" :: "600" :: "700" :: "800" :: "900" :: "1000" :: "1100" :: "1200" :: Nil),
                        "currentNumber" -> toJson(panelInfo.getPanelSales),
                        "lastYearNumber" -> toJson(panelInfo.getLastYearPanelSales)
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
