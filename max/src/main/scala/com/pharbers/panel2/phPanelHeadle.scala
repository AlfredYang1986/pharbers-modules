package com.pharbers.panel2

import com.pharbers.panel2.common.{phPanelFactory, phPanelTrait}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 18-4-3.
  */
case class phPanelHeadle(args: Map[String, List[String]]) extends phPanelTrait {
    val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
    val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
    val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
    val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head

    private val pf = phPanelFactory(company)

    def getMarkets = pf.getMarkets.map(_.get)

    override def calcYM: JsValue = {

        pf.getInstance.calcYM
    }

    override def getPanelFile(ym: List[String], mktArg: String = ""): JsValue = {

        val result = pf.getMarkets.map{ mkt =>
            mkt.get -> pf.getInstance(mkt).getPanelFile(ym, mkt.get)
        }.toMap
        toJson(result)
    }

}