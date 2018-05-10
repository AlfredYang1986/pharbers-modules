package com.pharbers.channel

import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.ErrorCode._
import com.pharbers.builder.phBuilder
import com.pharbers.calc.phMaxJob
import com.pharbers.channel.chanelImpl.responsePusher
import com.pharbers.channel.doJobActor._
import com.pharbers.common.algorithm.alTempLog
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by spark on 18-4-26.
  */
object doJobActor {
    def name = "doJob"
    def props = Props[doJobActor]

    case class msg_doYmCalc(js: JsValue)
    case class msg_doPanel(js: JsValue)
    case class msg_doCalc(js: JsValue)
    case class msg_doKill(js: JsValue)
}

class doJobActor extends Actor with ActorLogging with sendEmTrait {
    implicit val acc: Actor = this

    override def receive = {
        case msg_doYmCalc(jv) => doYmCalc(jv)
        case msg_doPanel(jv) => doPanel(jv)
        case msg_doCalc(jv) => doCalc(jv)
        case msg_doKill(jv) => doKill(jv)
        case _ => ???
    }

    def doYmCalc(jv: JsValue): Unit = {
        try{
            // TODO 写死的testUser去掉
            val company = "testGroup"//(jv \ "company_id").asOpt[String].get
            val user = "testUser"//(jv \ "user_id").asOpt[String].get
            val args = (jv \ "args").asOpt[String].get
                        .tail.init
                        .split(",").map(_.split("="))
                        .map(x => x.head.trim -> x.last.trim)
                        .toMap

            alTempLog(s"doYmCalc, company is = $company, user is = $user")
            sendMessage(company, user, "ymCalc", "start", toJson(Map("progress" -> toJson("0"))))

            val ymLst = phBuilder(company, user).set(args).doCalcYM

            alTempLog("计算月份完成, result = " + ymLst)
            sendMessage(company, user, "ymCalc", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("ymList" -> ymLst)))))
            responsePusher().callJobResponse(ymLst, "done")(jv)// send Kafka message
        } catch {
            case ex: Exception => sendError("testGroup", "testUser", "ymCalc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doPanel(jv: JsValue): Unit = {
        try{
            // TODO 写死的testUser去掉
            val company = "testGroup"//(jv \ "company_id").asOpt[String].get
            val user = "testUser"//(jv \ "user_id").asOpt[String].get
            val args = (jv \ "args").asOpt[String].get
                        .tail.init
                        .split(",").map(_.split("="))
                        .map(x => x.head -> x.last)
                        .toMap

            alTempLog(s"doPanel, company is = $company, user is = $user")
            sendMessage(company, user, "panel", "start", toJson(Map("progress" -> toJson("0"))))


            val panel = phBuilder(company, user).set(args)
                    .set("yms", "201711#201712")
                    .doPanel


            alTempLog("生成panel完成, result = " + panel)
            sendMessage(company, user, "panel", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("panel" -> panel)))))
            responsePusher().callJobResponse(panel, "done")(jv)// send Kafka message
        } catch {
            case ex: Exception => sendError("testGroup", "testUser", "panel", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doCalc(jv: JsValue): Unit = {
        try {
            sendMessage("testGroup", "testUser", "calc", "start", toJson(Map("progress" -> toJson("0"))))

            val args =
                (jv \ "args").asOpt[String].get
                        .tail.init
                        .split(",").map(_.split("="))
                        .map(x => x.head -> x.last)
                        .toMap

            val result = phMaxJob("1bd61350-9d4d-47af-a50c-450c77bcdb67", "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").asInstanceOf[StringArgs].get

            println("计算完成, result = " + result)

            responsePusher().callJobResponse(toJson(result), "done")(jv)// send Kafka message
            sendMessage("testGroup", "testUser", "calc", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("calc" -> toJson(result))))))
        } catch {
            case ex: Exception => sendError("testGroup", "testUser", "calc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doKill(jv: JsValue): Unit = ???
}
