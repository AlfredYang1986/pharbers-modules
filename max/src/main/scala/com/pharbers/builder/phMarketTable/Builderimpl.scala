package com.pharbers.builder.phMarketTable

import akka.actor.Actor
import com.pharbers.pactions.actionbase.pActionTrait

case class Builderimpl(company: String) extends phMarketDBTrait with
        phMarketReflectTrait with phMarketManager with phReflectCheck {

    val mktLst: List[String] = getAllMarkets(company)

    def getYmInst: Map[String, String] = queryInstance(company, mktLst.head)(onlyYmInst).get
    def getPanelInst(mkt: String): Map[String, String] = queryInstance(company, mkt)(onlyPanelInst).get
    def getMaxInst(mkt: String): Map[String, String] = queryInstance(company, mkt)(onlyMaxInst).get
    def getDeliveryInst(mkt: String): Map[String, String] = queryInstance(company, mkt)(onlyDeliveryInst).get

    def impl(clazz: String, initArgs: Map[String, String])(implicit _actor: Actor): pActionTrait = {
        val constructor = Class.forName(clazz).getConstructors()(0)
        constructor.newInstance(initArgs, _actor).asInstanceOf[pActionTrait]
    }

    def implWithoutActor(clazz: String, initArgs: Map[String, String]): pActionTrait = {
        val constructor = Class.forName(clazz).getConstructors()(0)
        constructor.newInstance(initArgs).asInstanceOf[pActionTrait]
    }
}

