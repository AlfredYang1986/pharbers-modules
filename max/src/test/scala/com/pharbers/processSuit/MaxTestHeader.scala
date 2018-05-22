package com.pharbers.processSuit

import akka.actor.{Actor, Props}
import com.pharbers.builder.phBuilder

object MaxTestHeader {
    def props(company: String, user: String, jobId: String): Props =
        Props(new MaxTestHeader(company, user, jobId))

    case class calcYm(cpa: String, gycx: String)
    case class panel(cpa: String, gycx: String, yms: String)
    case class max()
}

class MaxTestHeader(company: String, user: String, jobId: String) extends Actor {
    implicit val actor: Actor = this
    import com.pharbers.processSuit.MaxTestHeader._

    override def receive: Receive = {
        case calcYm(cpa, gycx) =>
            sender ! phBuilder(company, user, jobId)
                    .set("cpa", cpa).set("gycx", gycx)
                    .doCalcYM

        case panel(cpa, gycx, yms) =>
            sender ! phBuilder(company, user, jobId)
                    .set("cpa", cpa).set("gycx", gycx)
                    .set("yms", yms)
                    .doPanel

        case max() =>
            sender ! phBuilder(company, user, jobId).doMax

        case _ => ???
    }
}