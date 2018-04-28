package com.pharbers.main.callJobRequest

import com.pharbers.ErrorCode._
import play.api.libs.json.JsValue
import com.pharbers.calc.phMaxJob
import play.api.libs.json.Json.toJson
import com.pharbers.spark.phSparkDriver
import com.pharbers.channel.responsePusher
import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.main.callJobRequest.doJobActor._
import com.pharbers.panel.nhwa.{phNhwaCalcYMJob, phNhwaPanelJob}
import com.pharbers.pactions.actionbase.{JVArgs, MapArgs, StringArgs}

/**
  * Created by spark on 18-4-26.
  */
object doJobActor {
    def props = Props[doJobActor]
    def name = "doJob"

    case class msg_doYmCalc(js: JsValue)
    case class msg_doPanel(js: JsValue)
    case class msg_doCalc(js: JsValue)
    case class msg_doKill(js: JsValue)
}

class doJobActor extends Actor with ActorLogging with sendEmTrait {

    override def receive = {
        case msg_doYmCalc(jv) => doYmCalc(jv)
        case msg_doPanel(jv) => doPanel(jv)
        case msg_doCalc(jv) => doCalc(jv)
        case msg_doKill(jv) => doKill(jv)
        case _ => ???
    }

    def doYmCalc(jv: JsValue): Unit = {
        try{
            sendMessage("testUser", "ymCalc", "start", toJson(Map("progress" -> toJson("0"))))

            val args = (jv \ "args").asOpt[String].get
                        .tail.init
                        .split(",").map(_.split("="))
                        .map(x => x.head.trim -> x.last.trim)
                        .toMap
            val result = "ymcalc" /*(args("company") match {
                case "nhwa" => phNhwaCalcYMJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx").perform()
                case _ => ???
            }).asInstanceOf[JVArgs].get*/

            // TODO 暂时先放这，肯定不好
            phSparkDriver().ss.stop
            println("计算月份完成, result = " + result)

            responseJob(toJson(result))(jv) // send Kafka message
            sendMessage("testUser", "ymCalc", "done", toJson(Map("content" -> toJson(Map("ymList" -> result)))))
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

            val result = "panle" /*(args("company") match {
                case "nhwa" => phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob")
                case _ => ???
            }).asInstanceOf[StringArgs].get*/

            // TODO 暂时先放这，肯定不好
            phSparkDriver().ss.stop
            println("生成panel完成, result = " + result)

            responseJob(toJson(result))(jv)
            sendMessage("testUser", "panel", "done", toJson(Map("content" -> toJson(Map("panel" -> toJson(result))))))
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

            val result = "calc"// phMaxJob("b87579dd-1cb7-4c17-aa34-685fae0d3541", "nhwa/universe_麻醉市场_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").asInstanceOf[StringArgs].get

            // TODO 暂时先放这，肯定不好
            phSparkDriver().ss.stop
            println("计算完成, result = " + result)

            responseJob(toJson(result))(jv)
            sendMessage("testUser", "calc", "done", toJson(Map("content" -> toJson(Map("calc" -> toJson(result))))))
        } catch {
            case ex: Exception => sendError("testUser", "calc", toJson(Map("code" -> toJson(getErrorCodeByName(ex.getMessage)), "message" -> toJson(ex.getMessage))))
        }
    }

    def doKill(jv: JsValue): Unit = ???

    def responseJob(result: JsValue)(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {
        responsePusher().callJobResponse(result, "done")(jv)
        (Some(Map().empty), None)
    }
}
