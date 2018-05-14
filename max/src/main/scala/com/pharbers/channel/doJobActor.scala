package com.pharbers.channel

import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.ErrorCode._
import com.pharbers.builder.phBuilder
import com.pharbers.channel.chanelImpl.responsePusher
import com.pharbers.channel.doJobActor._
import com.pharbers.common.algorithm.alTempLog
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
        // TODO 写死的testUser去掉
        val company = "testGroup"//(jv \ "company_id").asOpt[String].get
        val user = "testUser"//(jv \ "user_id").asOpt[String].get
        val job_id = "testJobId"//(jv \ "job_id").asOpt[String].get
        val args = (jv \ "args").asOpt[String].get
                .tail.init
                .split(",").map(_.split("="))
                .map(x => x.head.trim -> x.last.trim)
                .toMap

        try{
            alTempLog(s"doYmCalc, company is = $company, user is = $user")
            sendMessage(company, user, "ymCalc", "start", toJson(Map("progress" -> toJson("0"))))

//            val ymLst = phBuilder(company, user, job_id)
//                    .set(args)
//                    .set("cpa", "/mnt/config/Client/180211恩华17年1-12月检索.xlsx")
//                    .doCalcYM

            val ymLst = toJson("201711#201712")

            alTempLog("计算月份完成, result = " + ymLst)
            sendMessage(company, user, "ymCalc", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("ymList" -> ymLst)))))
            responsePusher().callJobResponse(ymLst, "done")(jv)// send Kafka message
        } catch {
            case ex: Exception => sendError(company, user, "ymCalc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doPanel(jv: JsValue): Unit = {
        // TODO 写死的testUser去掉
        val company = "testGroup"//(jv \ "company_id").asOpt[String].get
        val user = "testUser"//(jv \ "user_id").asOpt[String].get
        val job_id = "testJobId"//(jv \ "job_id").asOpt[String].get
        val args = (jv \ "args").asOpt[String].get
                .tail.init
                .split(",").map(_.split("="))
                .map(x => x.head -> x.last)
                .toMap

        try{
            alTempLog(s"doPanel, company is = $company, user is = $user")
            sendMessage(company, user, "panel", "start", toJson(Map("progress" -> toJson("0"))))

//            phBuilder(company, user, job_id).set(args)
//                    .set("yms", "201711#201712")
//                    .doPanel

            alTempLog("生成panel完成")
            sendMessage(company, user, "panel", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("panel" -> toJson(job_id))))))
            responsePusher().callJobResponse(toJson(job_id), "done")(jv)// send Kafka message
        } catch {
            case ex: Exception => sendError(company, user, "panel", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doCalc(jv: JsValue): Unit = {
        // TODO 写死的testUser去掉
        val company = "testGroup"//(jv \ "company_id").asOpt[String].get
        val user = "testUser"//(jv \ "user_id").asOpt[String].get
        val job_id = "testJobId"//(jv \ "job_id").asOpt[String].get
        val args = (jv \ "args").asOpt[String].get
                .tail.init
                .split(",").map(_.split("="))
                .map(x => x.head -> x.last)
                .toMap

        try {
            alTempLog(s"doCalc, company is = $company, user is = $user")
            sendMessage(company, user, "calc", "start", toJson(Map("progress" -> toJson("0"))))

            phBuilder(company, user, job_id).doMax

            alTempLog("计算完成")
            sendMessage(company, user, "calc", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("calc" -> toJson(job_id))))))
            responsePusher().callJobResponse(toJson(job_id), "done")(jv)// send Kafka message
        } catch {
            case ex: Exception => sendError(company, user, "calc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doKill(jv: JsValue): Unit = ???
}