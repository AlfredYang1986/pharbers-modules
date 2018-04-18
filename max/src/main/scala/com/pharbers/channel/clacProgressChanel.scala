package com.pharbers.channel

import akka.actor.ActorSystem
import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.channel.chanelImpl._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

case class clacProgressChannel(override val group_id : String)
                              (override implicit val dispatch: ActorSystem)
                                        extends kafkaBasicConf
                                           with kafkaPushRecord
                                           with kafkaConsumer
                                           with kafkaLstTopics
                                           with kafkaPushTopic
                                           with PharbersInjectModule {

    override val id: String = "progress-channel-config"
    override val configPath: String = "pharbers_config/pharbers-channel-config.xml"
    override val md = "endpoints" :: "schemapath" :: "topic" :: Nil

    override lazy val endpoints: String = config.mc.find(p => p._1 == "endpoints").get._2.toString
    override lazy val schemapath: String = config.mc.find(p => p._1 == "schemapath").get._2.toString
    override lazy val topic: String = config.mc.find(p => p._1 == "topic").get._2.toString

    override val consumeHandler: JsValue => MessageRoutes = { jv =>
        import com.pharbers.bmpattern.LogMessage.common_log
        import com.pharbers.bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("progress channel"))), jv)
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(None))
    }
}
