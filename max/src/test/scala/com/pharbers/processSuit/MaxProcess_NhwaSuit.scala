package com.pharbers.processSuit

import java.util.Date

import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration._
import java.text.SimpleDateFormat

import play.api.libs.json.JsValue
import akka.actor.{ActorRef, ActorSystem}

class MaxProcess_NhwaSuit extends FunSuite {
    val system = ActorSystem("maxActor")
    val company: String = "5afa53bded925c05c6f69c54"
    val user: String = "5afaa333ed925c30f8c066d1"
    val jobId: String = "20180615nhwa002"
    val testActor: ActorRef = system.actorOf(MaxTestHeader.props(company, user, jobId))
    import com.pharbers.processSuit.MaxTestHeader._

    test("nhwa max process test") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"开始时间" + dateformat.format(new Date()))
        println()
        val cpa = "Source/180211恩华17年1-12月检索.xlsx"
        val gycx = ""
        val yms = "201710"


        implicit val t: Timeout = 20 minutes

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