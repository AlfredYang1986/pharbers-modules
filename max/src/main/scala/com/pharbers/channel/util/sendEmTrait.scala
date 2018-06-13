package com.pharbers.channel.util

import java.util.Date

import com.pharbers.common.xmpp.em.emDriver
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by spark on 18-4-27.
  */
trait sendEmTrait {
    val ed: emDriver = emDriver()

    def sendMessage(targetGroup: String, targetUser: String, callJob: String,
                    jobStage: String, attributes: JsValue): Unit = {
        val msg = toJson(toJson(
            Map(
                "messageType" -> toJson("application/json"), // 消息类型
                "date" -> toJson(new Date().getTime.toString), //时间，发送消息是的时间戳
                "call" -> toJson(callJob), //job 类型
                "stage" -> toJson(jobStage), //标记消息状态，开始start、进行中ing、错误error、结束done
                "target" -> toJson(targetUser), //群组内那个人处理
                "attributes" -> attributes
            )
        ).toString)

        ed.sendMessage2Group(targetGroup, msg)
    }

    def sendError(targetGroup: String, targetUser: String, callJob: String, errorMsg: JsValue): Unit = {
        val msg = toJson(toJson(
            Map(
                "messageType" -> toJson("application/json"), // 消息类型
                "date" -> toJson(new Date().getTime.toString), //时间，发送消息是的时间戳
                "call" -> toJson(callJob), //job 类型
                "stage" -> toJson("error"), //标记消息状态，开始start、进行中ing、错误error、结束done
                "target" -> toJson(targetUser), //群组内那个人处理
                "attributes" -> toJson(Map("progress" -> toJson("0"))),
                "error" -> errorMsg
            )
        ).toString)

        ed.sendMessage2Group(targetGroup, msg)
    }
}
