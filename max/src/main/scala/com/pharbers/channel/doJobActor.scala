package com.pharbers.channel

import com.pharbers.ErrorCode._
import com.pharbers.calc.phMaxJob
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.channel.doJobActor._
import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.channel.chanelImpl.responsePusher
import com.pharbers.common.algorithm.{alTempLog, max_path_obj}
import com.pharbers.panel.nhwa.{phNhwaCalcYMJob, phNhwaPanelJob}
import com.pharbers.pactions.actionbase.{JVArgs, MapArgs, StringArgs}

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
            val companyId = (jv \ "company_id").asOpt[String].get
            val userId = (jv \ "user_id").asOpt[String].get
            val args = (jv \ "args").asOpt[String].get
                        .tail.init
                        .split(",").map(_.split("="))
                        .map(x => x.head.trim -> x.last.trim)
                        .toMap

            alTempLog(s"doYmCalc, companyId is = $companyId, userId is = $userId")
            // TODO 写死的testUser去掉
            sendMessage("testGroup", "testUser", "ymCalc", "start", toJson(Map("progress" -> toJson("0"))))

            // TODO 写成Builder形式
            val result = phNhwaCalcYMJob(max_path_obj.p_clientPath + args("cpa")).perform().asInstanceOf[JVArgs].get

            alTempLog("计算月份完成, result = " + result)

            responsePusher().callJobResponse(result, "done")(jv)// send Kafka message
            sendMessage("testGroup", "testUser", "ymCalc", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("ymList" -> result)))))
        } catch {
            case ex: Exception => sendError("testUser", "ymCalc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doPanel(jv: JsValue): Unit = {
        try{
            sendMessage("testUser", "panel", "start", toJson(Map("progress" -> toJson("0"))))

            val args =
                (jv \ "args").asOpt[String].get
                        .tail.init
                        .split(",").map(_.split("="))
                        .map(x => x.head -> x.last)
                        .toMap

            val result = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob")
                .asInstanceOf[StringArgs].get

            println("生成panel完成, result = " + result)

            responsePusher().callJobResponse(toJson(result), "done")(jv)// send Kafka message
            sendMessage("testUser", "panel", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("panel" -> toJson(result))))))
        } catch {
            case ex: Exception => sendError("testUser", "panel", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doCalc(jv: JsValue): Unit = {
        try {
            sendMessage("testUser", "calc", "start", toJson(Map("progress" -> toJson("0"))))

            val args =
                (jv \ "args").asOpt[String].get
                        .tail.init
                        .split(",").map(_.split("="))
                        .map(x => x.head -> x.last)
                        .toMap

            val result = phMaxJob("1bd61350-9d4d-47af-a50c-450c77bcdb67", "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").asInstanceOf[StringArgs].get

            println("计算完成, result = " + result)

            responsePusher().callJobResponse(toJson(result), "done")(jv)// send Kafka message
            sendMessage("testUser", "calc", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("calc" -> toJson(result))))))
        } catch {
            case ex: Exception => sendError("testUser", "calc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doKill(jv: JsValue): Unit = ???
}
