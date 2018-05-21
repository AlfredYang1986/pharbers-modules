package com.pharbers.panel

import java.util.Date
import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.FunSuite
import scala.concurrent.Await
import scala.concurrent.duration._
import java.text.SimpleDateFormat
import play.api.libs.json.JsValue
import com.pharbers.builder.phBuilder
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

class PfizerPanelSuit extends FunSuite {
    val system = ActorSystem("maxActor")
    val testActor: ActorRef = system.actorOf(PfizerPanelTestImpl.props)

    test("Pfizer calc ym") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"筛选月份开始时间" + dateformat.format(new Date()))
        println()

        implicit val t: Timeout = 10 minutes
        val r = testActor ? "calcYM"
        val result = Await.result(r.mapTo[JsValue], t.duration)


        println("result = " + result)
        println()
        println(s"筛选月份结束时间" + dateformat.format(new Date()))
    }


    test("Pfizer panel generator") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
        println()


        implicit val t: Timeout = 60 minutes
        val r = testActor ? "panel"
        val result = Await.result(r.mapTo[JsValue], t.duration)


        println("result = " + result)
        println()
        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
    }

}

object PfizerPanelTestImpl {
    def props: Props = Props[PfizerPanelTestImpl]
}

class PfizerPanelTestImpl extends Actor {
    implicit val actor: Actor = this
    val company: String = "5b028f95ed925c2c705b85ba"
    val user: String = "5b028feced925c2c705b85bb"
    val jobId: String = "5b029006ed925c2c705b85bd"

    override def receive: Receive = {
        case "calcYM" =>
            sender ! phBuilder(company, user, jobId)
                    .set("cpa", "astl_cpa-10.xlsx")
                    .set("gycx", "astl_gycx_1-10.xlsx")
                    .doCalcYM
        case "panel" =>
            sender ! phBuilder(company, user, jobId)
                    .set("cpa", "astl_cpa-10.xlsx")
                    .set("gycx", "astl_gycx_1-10.xlsx")
                    .set("yms", "201710")
                    .doPanel
        case _ => ???
    }
}
