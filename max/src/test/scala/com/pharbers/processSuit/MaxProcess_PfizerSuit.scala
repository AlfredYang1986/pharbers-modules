//package com.pharbers.max
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import akka.actor.{ActorRef, ActorSystem}
//import akka.pattern.ask
//import akka.util.Timeout
//import com.pharbers.processSuit.MaxTestHeader
//import org.scalatest.FunSuite
//import play.api.libs.json.JsValue
//
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//class MaxProcess_PfizerSuit extends FunSuite {
//    val system = ActorSystem("maxActor")
//    val company: String = "5b028f95ed925c2c705b85ba"
//    val user: String = "5b028feced925c2c705b85bb"
//    val jobId: String = "20180524pfizer001"
//    val testActor: ActorRef = system.actorOf(MaxTestHeader.props(company, user, jobId))
//    import com.pharbers.processSuit.MaxTestHeader._
//
//    test("pfizer max process test") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"开始时间" + dateformat.format(new Date()))
//        println()
//        val cpa = "pfizer/1802 CPA.xlsx"
//        val gycx = "pfizer/1802 GYC.xlsx"
//        val yms = "201802"
//
//
//        implicit val t: Timeout = 600 minutes
//
//        val r = testActor ? calcYm(cpa, gycx)
//        val result = Await.result(r.mapTo[JsValue], t.duration)
//        println("calcYm result = " + result)
//        println(s"calcYm 结束时间" + dateformat.format(new Date()))
//
//        val r2 = testActor ? panel(cpa, gycx, yms)
//        val result2 = Await.result(r2.mapTo[JsValue], t.duration)
//        println("panel result2 = " + result2)
//        println(s"panel 结束时间" + dateformat.format(new Date()))
//
//        val r3 = testActor ? max()
//        val result3 = Await.result(r3.mapTo[JsValue], t.duration)
//        println("max result3 = " + result3)
//        println(s"max 结束时间" + dateformat.format(new Date()))
//
//
//        println()
//        println(s"结束时间" + dateformat.format(new Date()))
//    }
//}
