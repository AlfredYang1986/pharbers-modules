package com.pharbers.max

import java.util.{Date, UUID}

import scala.concurrent.duration._
import akka.pattern.ask
import org.scalatest.FunSuite
import java.text.SimpleDateFormat

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import com.pharbers.builder.phBuilder
import com.pharbers.calc.phMaxJob
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs}
import com.pharbers.panel.pfizer.phPfizerPanelJob
import play.api.libs.json.JsValue

import scala.concurrent.Await

/**
  * Created by jeorch on 18-4-10.
  */
class MaxCalcSuite extends FunSuite{
    val system = ActorSystem("maxActor")

    val company1: String = "5afa53bded925c05c6f69c54"
    val user1: String = "5afaa333ed925c30f8c066d1"

    val company2: String = "5afa53bded925c05c6f69c54"
    val user2: String = "5afaa333ed925c30f8c066d1"

    lazy val NhwaActor = system.actorOf(MaxTestImpl.props(company1, user1))
    lazy val PfizerActor = system.actorOf(MaxTestImpl.props(company2, user2))

    test("nhwa calc test") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"筛选月份开始时间" + dateformat.format(new Date()))
        println()

        implicit val t: Timeout = 20 minutes
        val r = NhwaActor ? "calcYM"
        val result = Await.result(r.mapTo[JsValue], t.duration)
        println("result = " + result)
        val r2 = NhwaActor ? "panel"
        val result2 = Await.result(r2.mapTo[JsValue], t.duration)
        println("result2 = " + result2)
        val r3 = NhwaActor ? "max"
        val result3 = Await.result(r3.mapTo[JsValue], t.duration)
        println("result3 = " + result3)


        println()
        println(s"筛选月份结束时间" + dateformat.format(new Date()))
    }

    test("pfizer calc test") {

        val job_id = "5b029006ed925c2c705b85bd"
        val user_id = "5b028feced925c2c705b85bb"
        val company = "5b028f95ed925c2c705b85ba"
        val ym = "201802"
        val mkt = "INF"
        val cpa = "/mnt/config/Client/pfizer/1802 CPA.xlsx"
        val gyc = "/mnt/config/Client/pfizer/1802 GYC.xlsx"

        var args:Map[String, String] = Map(
                "cpa" -> cpa,
                "gyc" -> gyc,
                "ym" -> ym,
                "mkt" -> mkt,
                "user" -> user_id,
                "company" -> company,
                "universe_file" -> s"pfizer/universe_${mkt}_online.xlsx",
                "job_id" -> job_id
            )

        val panelResult = phPfizerPanelJob(args).perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
        println("panelResult = " + panelResult)

        args += "panel_name" -> panelResult.toString

        val result = phMaxJob(args).perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//        val result = phMaxJob(s"${mkt}_panel_1802.csv", s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get

        println("result = " + result)
    }

//    test("SpecialMarket DVP calc test") {
//        val mkt = "DVP"
//
////        val panelResult = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", s"${mkt}").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
////        println("panelResult = " + panelResult)
////        val result = phMaxJobForPfizerDVP(panelResult.toString, s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//        val result = phMaxJobForPfizerDVP(s"${mkt}_panel_1802.csv", s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//
//        println("result = " + result)
//    }
//
//    test("SpecialMarket CNS_R calc test") {
//        val mkt = "CNS_R"
//
//        val panelResult = phPfizerPanelJob("/mnt/config/Client/pfizer/1802 CPA.xlsx", "/mnt/config/Client/pfizer/1802 GYC.xlsx", "201802", s"${mkt}").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
//        println("panelResult = " + panelResult)
//
//        val result = phMaxJobForPfizerCNS_R(panelResult.toString, s"pfizer/universe_${mkt}_online.xlsx").perform().asInstanceOf[MapArgs].get("max_persistent_action").get
//
//        println("result = " + result)
//    }

}

object MaxTestImpl {
    def props(company_arg: String, user_arg: String) = Props(new MaxTestImpl(company_arg, user_arg))
}

class MaxTestImpl(company_arg: String, user_arg: String) extends Actor {
    implicit val actor: Actor = this
    val company: String = company_arg
    val user: String = user_arg
    val jobId: String = "20180518test001"

    override def receive: Receive = {
        case "calcYM" =>
            sender ! phBuilder(company, user, jobId).set("cpa", "180211恩华17年1-12月检索.xlsx").doCalcYM
        case "panel" =>
            sender ! phBuilder(company, user, jobId)
                .set("cpa", "180211恩华17年1-12月检索.xlsx")
                .set("yms", "201701#201702#201703#201704#201705#201706#201707#201708#201709#201710#201711#201712#")
                .doPanel
        case "max" =>
            sender ! phBuilder(company, user, jobId).doMax
        case _ => ???
    }
}