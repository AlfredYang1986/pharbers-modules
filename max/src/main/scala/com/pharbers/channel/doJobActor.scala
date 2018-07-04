package com.pharbers.channel

import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.ErrorCode._
import com.pharbers.builder.phBuilder
import com.pharbers.channel.chanelImpl.responsePusher
import com.pharbers.channel.doJobActor._
import com.pharbers.channel.util.{getJV2Map, sendEmTrait}
import com.pharbers.common.algorithm.alTempLog
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by spark on 18-4-26.
  */
object doJobActor {
    def name = "doJob"
    def props: Props = Props[doJobActor]

    case class msg_doYmCalc(js: JsValue)
    case class msg_doPanel(js: JsValue)
    case class msg_doCalc(js: JsValue)
    case class msg_doKill(js: JsValue)
}

class doJobActor extends Actor with ActorLogging with sendEmTrait with getJV2Map {
    implicit val acc: Actor = this

    override def receive: PartialFunction[Any, Unit] = {
        case msg_doYmCalc(jv) => doYmCalc(jv)
        case msg_doPanel(jv) => doPanel(jv)
        case msg_doCalc(jv) => doCalc(jv)
        case msg_doKill(jv) => doKill(jv)
        case _ => ???
    }

    def doYmCalc(jv: JsValue): Unit = {
        val company = (jv \ "company_id").asOpt[String].get
        val user = (jv \ "user_id").asOpt[String].get
        val args = getArgs2Map(jv)

        try{
            alTempLog(s"doYmCalc, company is = $company, user is = $user")
            sendMessage(company, user, "ymCalc", "start", toJson(Map("progress" -> toJson("0"))))

            val ymLst = phBuilder(company, user, args("job_id")).set(args).doCalcYM()

            alTempLog("计算月份完成, result = " + ymLst)
            sendMessage(company, user, "ymCalc", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("ymList" -> ymLst)))))
            responsePusher().callJobResponse(Map("job_id" -> args("job_id")), "done")(jv)// send Kafka message
        } catch {
            case ex: Exception => sendError(company, user, "ymCalc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doPanel(jv: JsValue): Unit = {
        val company = (jv \ "company_id").asOpt[String].get
        val user = (jv \ "user_id").asOpt[String].get
        val args = getArgs2Map(jv)

        try{
            alTempLog(s"doPanel, company is = $company, user is = $user")
            sendMessage(company, user, "panel", "start", toJson(Map("progress" -> toJson("0"))))

            phBuilder(company, user, args("job_id")).set(args).doPanel()

            alTempLog("生成panel完成")
            sendMessage(company, user, "panel", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("panel" -> toJson(args("job_id")))))))
            responsePusher().callJobResponse(Map("job_id" -> args("job_id")), "done")(jv)// send Kafka message
        } catch {
            case ex: Exception => sendError(company, user, "panel", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doCalc(jv: JsValue): Unit = {
        val company = (jv \ "company_id").asOpt[String].get
        val user = (jv \ "user_id").asOpt[String].get
        val job_id = getArgs2Map(jv)("job_id")

        try {
            alTempLog(s"doCalc, company is = $company, user is = $user")
            sendMessage(company, user, "calc", "start", toJson(Map("progress" -> toJson("0"))))

            phBuilder(company, user, job_id).doMax()

            alTempLog("计算完成")
            sendMessage(company, user, "calc", "done", toJson(Map("progress" -> toJson("100"), "content" -> toJson(Map("calc" -> toJson(job_id))))))
            responsePusher().callJobResponse(Map("job_id" -> job_id), "done")(jv)// send Kafka message
        } catch {
            case ex: Exception => sendError(company, user, "calc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doKill(jv: JsValue): Unit = ???
}
