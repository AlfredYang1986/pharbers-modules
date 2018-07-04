package com.pharbers.processSuit

import java.util.Date

import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.FunSuite
import java.text.SimpleDateFormat

import play.api.libs.json.JsValue
import akka.actor.{ActorRef, ActorSystem}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class MaxProcess_PfizerSuit extends FunSuite {
    val system = ActorSystem("maxActor")
    val company: String = "5b028f95ed925c2c705b85ba"
    val user: String = "5b028feced925c2c705b85bb"
    val jobId: String = "20180628pfizer001"
    val testActor: ActorRef = system.actorOf(MaxTestHeader.props(company, user, jobId))
    import com.pharbers.processSuit.MaxTestHeader._

    test("pfizer max process test") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"开始时间" + dateformat.format(new Date()))
        println()
        val cpa = "Source/pfizer/1804 CPA.xlsx"
        val gycx = "Source/pfizer/1804 GYC.xlsx"
        val yms = "201804"


        implicit val t: Timeout = 600 minutes

        val r = testActor ? calcYm(cpa, gycx)
        val result = Await.result(r.mapTo[JsValue], t.duration)
        println("calcYm result = " + result)
        println(s"calcYm 结束时间" + dateformat.format(new Date()))

        val r2 = testActor ? panel(cpa, gycx, yms)
        val result2 = Await.result(r2.mapTo[JsValue], t.duration)
        println("panel result2 = " + result2)
        println(s"panel 结束时间" + dateformat.format(new Date()))

        val r3 = testActor ? max()
        val result3 = Await.result(r3.mapTo[JsValue], t.duration)
        println("max result3 = " + result3)
        println(s"max 结束时间" + dateformat.format(new Date()))

        println()
        println(s"结束时间" + dateformat.format(new Date()))
    }
}
