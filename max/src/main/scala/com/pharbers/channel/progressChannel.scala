package com.pharbers.channel

import akka.actor.ActorSystem
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.channel.chanelImpl._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

case class progressChannel(override val group_id: String)
                          (override implicit val dispatch: ActorSystem)
        extends kafkaBasicConf with kafkaConsumer with kafkaPushRecord {

    override lazy val endpoints: String = kafka_config_obj.endpoints
    override lazy val schemapath: String = kafka_config_obj.schemapath
    override lazy val topic: String = kafka_config_obj.topic

    override val consumeHandler: JsValue => MessageRoutes = { jv =>
        import com.pharbers.bmpattern.LogMessage.common_log
        import com.pharbers.bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("msg channel"))), jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(None))
    }
}
