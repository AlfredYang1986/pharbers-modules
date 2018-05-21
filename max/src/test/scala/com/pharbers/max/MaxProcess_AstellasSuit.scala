//package com.pharbers.max
//
//import java.util.Date
//
//import akka.pattern.ask
//import akka.util.Timeout
//import org.scalatest.FunSuite
//
//import scala.concurrent.Await
//import java.text.SimpleDateFormat
//import play.api.libs.json.JsValue
//import scala.concurrent.duration._
//import akka.actor.{ActorRef, ActorSystem}
//
//class MaxProcess_AstellasSuit extends FunSuite {
//    val system = ActorSystem("maxActor")
//    val company: String = "5b023787810c6e0268fe6ff6"
//    val user: String = "5b0237b7810c6e0268fe6ff7"
//    val jobId: String = "20180521astellas002"
//    val testActor: ActorRef = system.actorOf(MaxTestHeader.props(company, user, jobId))
//    import com.pharbers.max.MaxTestHeader._
//
//    test("astellas max process test") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"开始时间" + dateformat.format(new Date()))
//        println()
//        val cpa = "astl_cpa-10.xlsx"
//        val gycx = "astl_gycx_1-10.xlsx"
//        val yms = "201710"
//
//
//        implicit val t: Timeout = 20 minutes
//
//        val r = testActor ? calcYm(cpa, gycx)
//        val result = Await.result(r.mapTo[JsValue], t.duration)
//        println("calcYm result = " + result)
//
//        val r2 = testActor ? panel(cpa, gycx, yms)
//        val result2 = Await.result(r2.mapTo[JsValue], t.duration)
//        println("panel result2 = " + result2)
//
//        val r3 = testActor ? max()
//        val result3 = Await.result(r3.mapTo[JsValue], t.duration)
//        println("max result3 = " + result3)
//
//
//        println()
//        println(s"结束时间" + dateformat.format(new Date()))
//    }
//}
