package com.pharbers.panel

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.nhwa.{phNhwaCalcYMJob, phNhwaPanelJob}
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite
import play.api.libs.json.JsValue

import scala.concurrent.Await
import scala.concurrent.duration._

class NhwaPanelSuit extends FunSuite {
    val config = ConfigFactory.load("split-new-master")
    val system = ActorSystem("maxActor", config)
    val testActor = system.actorOf(NhwaPanelTestImpl.props)

    test("nhwa calc ym") {
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


    test("nhwa panel generator") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
        println()


        implicit val t: Timeout = 10 minutes
        val r = testActor ? "panel"
        val result = Await.result(r.mapTo[String], t.duration)


        println("result = " + result)
        println()
        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
    }
}

object NhwaPanelTestImpl {
    def props = Props[NhwaPanelTestImpl]
}

class NhwaPanelTestImpl extends Actor {
    implicit val actor: Actor = this

    override def receive: Receive = {
        case "calcYM" =>
            sender ! phNhwaCalcYMJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx").perform().get
        case "panel" =>
            sender ! phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
        case _ => ???
    }
}
