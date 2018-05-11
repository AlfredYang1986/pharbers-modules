//package com.pharbers.panel
//
//import java.util.Date
//
//import akka.pattern.ask
//import akka.util.Timeout
//import org.scalatest.FunSuite
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//import java.text.SimpleDateFormat
//import play.api.libs.json.JsValue
//
//import com.pharbers.builder.phBuilder
//import akka.actor.{Actor, ActorSystem, Props}
//
//class NhwaPanelSuit extends FunSuite {
//    val system = ActorSystem("maxActor")
//    val testActor = system.actorOf(NhwaPanelTestImpl.props)
//
//    test("nhwa calc ym") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"筛选月份开始时间" + dateformat.format(new Date()))
//        println()
//
//
//        implicit val t: Timeout = 10 minutes
//        val r = testActor ? "calcYM"
//        val result = Await.result(r.mapTo[JsValue], t.duration)
//
//
//        println("result = " + result)
//        println()
//        println(s"筛选月份结束时间" + dateformat.format(new Date()))
//    }
//
//
//    test("nhwa panel generator") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//
//
//        implicit val t: Timeout = 10 minutes
//        val r = testActor ? "panel"
//        val result = Await.result(r.mapTo[JsValue], t.duration)
//
//
//        println("result = " + result)
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//    }
//
//}
//
//object NhwaPanelTestImpl {
//    def props = Props[NhwaPanelTestImpl]
//}
//
//class NhwaPanelTestImpl extends Actor {
//    implicit val actor: Actor = this
//    val company: String = "testGroup"
//    val user: String = "testUser"
//
//    override def receive: Receive = {
//        case "calcYM" =>
//            sender ! phBuilder(company, user).set("cpa", "/mnt/config/Client/180211恩华17年1-12月检索.xlsx").doCalcYM
//        case "panel" =>
//            sender ! phBuilder(company, user)
//                    .set("cpa", "/mnt/config/Client/180211恩华17年1-12月检索.xlsx")
//                    .set("yms", "201711#201712")
//                    .doPanel
//        case _ => ???
//    }
//}
