package com.pharbers.panel

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.panel.common.phHanderXmlTrait

/**
  * Created by clock on 18-3-6.
  */
case class phPanelHeadle(args: Map[String, List[String]]) extends phHanderXmlTrait {
    override val configPath: String = "pharbers_config/panel_config.xml"

    val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
    val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
    val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
    val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head

    val markets: List[String] = getSecondNode(s"company_$company", "markets").split(",").toList

    def getMarkets: JsValue = toJson(markets.mkString(comma))

    def calcYM: JsValue = {
        val instance = getMktInstance(getSecondNode(s"company_$company", markets.head))
        instance.calcYM
    }

    def getPanelFile(ym: List[String], mkt: String = "", t: Int = 0, c: Int = 0): JsValue = {
        val totalGenerateNum = markets.length * ym.length
        var curGenerateNum = 0
        val result = markets.map{ mkt =>
            val instance = getMktInstance(getSecondNode(s"company_$company", mkt))
            val r = instance.getPanelFile(ym, mkt, totalGenerateNum, curGenerateNum)
            curGenerateNum += ym.length
            mkt -> r
        }.toMap
        toJson(result)
    }


    private def getMktInstance(clazz: String) = {
        val constructor = Class.forName(clazz).getConstructors()(0)
        val conArgs = args ++ Map("mkts" -> markets)
        constructor.newInstance(conArgs).asInstanceOf[phPanelTrait]
    }
}