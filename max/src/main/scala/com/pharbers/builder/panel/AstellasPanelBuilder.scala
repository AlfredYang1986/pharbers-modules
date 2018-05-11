package com.pharbers.builder.panel

import akka.actor.Actor
import com.pharbers.builder.PanelBuilder
import com.pharbers.panel.nhwa.phNhwaPanelJob
import com.pharbers.pactions.actionbase.pActionTrait

case class AstellasPanelBuilder(override val company: String, override val user: String)
                               (implicit override val actor: Actor) extends PanelBuilder(company, user) {

    override def instance: pActionTrait = {
        args("mkt") match {
            case "麻醉市场1" => phNhwaPanelJob(company, user)(args("ym"), args("mkt"),
                args("cpa"), args("currentJob").toInt, args("jobTotal").toInt)("nhwa/2017年未出版医院名单.xlsx",
                "nhwa/universe_麻醉市场_online.xlsx", "nhwa/nhwa匹配表.xlsx",
                "nhwa/补充医院.xlsx", "nhwa/通用名市场定义.xlsx")

            case _ => throw new Exception("input wrong")
        }
    }
}
