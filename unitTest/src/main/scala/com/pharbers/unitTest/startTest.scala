package com.pharbers.unitTest

import java.util.UUID

import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import com.pharbers.builder.MarketTable
import com.pharbers.pactions.actionbase.MapArgs
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.concurrent.Await

case class startTest() {

    val system = ActorSystem("unitTest")
    implicit val t: Timeout = 120 minutes

    val companyLst: List[Map[String, String]] =
        new MarketTable{}.marketTable
            .filter(_("company") == "5b028f95ed925c2c705b85ba")

    def doTest(): Unit = companyLst.foreach{ c =>
        val args = Map(
            "company" -> c("company"),
            "mkt" -> c("market"),
            "user" -> "user",
            "job_id" -> UUID.randomUUID().toString,
            "cpa" -> "pfizer/1802 CPA.xlsx",
            "gycx" -> "pfizer/1802 GYC.xlsx",
            "ym" -> "201802"
        )

        val testHeader: ActorRef = system.actorOf(UnitTestHeader.props())
        val r = testHeader ? UnitTestHeader.testJob(args)
        Await.result(r.mapTo[String], t.duration)
    }
}

object UnitTestHeader {
    def props(): Props = Props[UnitTestHeader]
    case class testJob(args: Map[String, String])
}

class UnitTestHeader extends Actor {
    override def receive: Receive = {
        case UnitTestHeader.testJob(args) =>
            resultCheckJob(args)(this).perform(MapArgs(Map().empty))
            sender() ! "test success"
    }
}