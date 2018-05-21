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

class AstellasPanelSuit extends FunSuite {
    val system = ActorSystem("maxActor")
    val testActor: ActorRef = system.actorOf(AstellasPanelTestImpl.props)

    test("Astellas calc ym") {
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


    test("Astellas panel generator") {
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

object AstellasPanelTestImpl {
    def props: Props = Props[AstellasPanelTestImpl]
}

class AstellasPanelTestImpl extends Actor {
    implicit val actor: Actor = this
    val company: String = "5b023787810c6e0268fe6ff6"
    val user: String = "5b0237b7810c6e0268fe6ff7"
    val jobId: String = "5b023c90810c6e0268fe6ff9"

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
