package com.pharbers.processSuit

import java.util.Date

import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.FunSuite

import scala.concurrent.Await
import java.text.SimpleDateFormat

import play.api.libs.json.JsValue

import scala.concurrent.duration._
import akka.actor.{ActorRef, ActorSystem}

import scala.language.postfixOps

class MaxProcess_AstellasSuit extends FunSuite {
    val system = ActorSystem("maxActor")
    val company: String = "5b023787810c6e0268fe6ff6"
    val user: String = "5b0237b7810c6e0268fe6ff7"
    val jobId: String = "20180620astellas002"
    val testActor: ActorRef = system.actorOf(MaxTestHeader.props(company, user, jobId))
    import com.pharbers.processSuit.MaxTestHeader._

    test("astellas max process test") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"开始时间" + dateformat.format(new Date()))
        println()
        val cpa = "Source/acn_cpa_1712.xlsx"
        val gycx = "Source/acn_gyc_1712.xlsx"
        val yms = "201712"


        implicit val t: Timeout = 120 minutes

        val r = testActor ? calcYm(cpa, gycx)
        val result = Await.result(r.mapTo[JsValue], t.duration)
        println("calcYm result = " + result)

        val r2 = testActor ? panel(cpa, gycx, yms)
        val result2 = Await.result(r2.mapTo[JsValue], t.duration)
        println("panel result2 = " + result2)

        val r3 = testActor ? max()
        val result3 = Await.result(r3.mapTo[JsValue], t.duration)
        println("max result3 = " + result3)


        println()
        println(s"结束时间" + dateformat.format(new Date()))
    }
}
