package com.pharbers.builder

import akka.actor.Actor
import com.pharbers.pactions.actionbase.pActionTrait

case class Builderimpl() extends MarketTable with CheckTrait {

    // GET INSTANCE
    val ymInst: (String, String, List[Map[String, String]]) => String = (c, m, l) =>
        l.find(x => c == x("company") && m == x("market")).get("ymInstance")
    val panelInst: (String, String, List[Map[String, String]]) => String = (c, m, l) =>
        l.find(x => c == x("company") && m == x("market")).get("panelInstance")
    val maxInst: (String, String, List[Map[String, String]]) => String = (c, m, l) =>
        l.find(x => c == x("company") && m == x("market")).get("maxInstance")
    val deliveryInst: (String, String, List[Map[String, String]]) => String = (c, m, l) =>
        l.find(x => c == x("company") && m == x("market")).get("deliveryInstance")

    def getClazz(company: String, market: String)
                (instanceFunc: (String, String, List[Map[String, String]]) => String): String =
        instanceFunc(company, market, marketTable)

    def impl(clazz: String, initArgs: Map[String, String])(implicit _actor: Actor): pActionTrait = {
        val constructor = Class.forName(clazz).getConstructors()(0)
        constructor.newInstance(initArgs, _actor).asInstanceOf[pActionTrait]
    }

    def implWithoutActor(clazz: String, initArgs: Map[String, String]): pActionTrait = {
        val constructor = Class.forName(clazz).getConstructors()(0)
        constructor.newInstance(initArgs).asInstanceOf[pActionTrait]
    }
}

