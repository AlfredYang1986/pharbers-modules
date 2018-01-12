package com.pharbers.panel

import play.api.libs.json.JsValue
import scala.collection.immutable.Map
import play.api.libs.json.Json.toJson
import com.pharbers.panel.util.phPanelHandle
import com.pharbers.panel.util.xml.phHanderXmlTrait

/**
  * Created by clock on 18-1-3.
  */
case class phGeneratePanel(args: Map[String, List[String]]) extends phHanderXmlTrait with phPanelHandle{
    override val configPath: String = "pharbers_config/data_parse_file.xml"

    val uid = args.getOrElse("uid", throw new Exception("no find uid arg")).head
    val company = args.getOrElse("company", throw new Exception("no find company arg")).head
    val cpa = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
    val gycx = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head

    val markets: List[String] = getSecondNode(s"company_$company", "markets").split(",").toList

    override def getMarkets: JsValue = {
        val instance = getMktInstance(getSecondNode(s"company_$company", markets.head))
        instance.getMarkets
    }

    override def calcYM: JsValue = {
        val instance = getMktInstance(getSecondNode(s"company_$company", markets.head))
        instance.calcYM
    }

    override def getPanelFile(ym: List[String], mkt: String = "", t: Int = 0, c: Int = 0): JsValue = {
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
        constructor.newInstance(conArgs).asInstanceOf[phPanelHandle]
    }
}