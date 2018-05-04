package com.pharbers.main.callJobRequest

import java.util.Date

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.common.xmpp.emDriver

/**
  * Created by spark on 18-4-27.
  */
trait sendEmTrait {
    val ed: emDriver = emDriver()

    def sendMessage(target: String, call: String, stage: String, attributes: JsValue): Unit = {
        val company = "testGroup"
        val msg = toJson(toJson(
            Map(
                "messageType" -> toJson("application/json"), // 消息类型
                "date" -> toJson(new Date().getTime.toString), //时间，发送消息是的时间戳
                "call" -> toJson(call), //job 类型
                "stage" -> toJson(stage), //标记消息状态，开始start、进行中ing、错误error、结束done
                "target" -> toJson(target), //群组内那个人处理
                "attributes" -> attributes
            )
        ).toString)

        ed.sendMessage2Group(company, msg)
    }

    def sendError(target: String, call: String, errorMsg: JsValue): Unit = {
        val company = "testGroup"
        val msg = toJson(toJson(
            Map(
                "messageType" -> toJson("application/json"), // 消息类型
                "date" -> toJson(new Date().getTime.toString), //时间，发送消息是的时间戳
                "call" -> toJson(call), //job 类型
                "stage" -> toJson("error"), //标记消息状态，开始start、进行中ing、错误error、结束done
                "target" -> toJson(target), //群组内那个人处理
                "attributes" -> toJson(Map("progress" -> toJson("0"))),
                "error" -> errorMsg
            )
        ).toString)

        ed.sendMessage2Group(company, msg)
    }

}
