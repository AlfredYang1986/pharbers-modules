package com.pharbers.channel.chanelImpl

import com.pharbers.channel.callJobRequestMessage._
import akka.actor.ActorSystem
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import java.util.concurrent.Executors
import com.pharbers.common.xmpp.kafka._
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage

case class callJobConsumer(override val group_id: String)
                          (override implicit val dispatch: ActorSystem)
        extends kafkaBasicConf with kafkaConsumer {

    override lazy val endpoints: String = kafka_config_obj.endpoints
    override lazy val schemapath: String = kafka_config_obj.requestSP
    override lazy val topic: String = kafka_config_obj.callJobTopic

    override val consumeHandler: JsValue => MessageRoutes = { jv =>
        import com.pharbers.bmpattern.LogMessage.common_log
        import com.pharbers.bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("call job request"))), jv)
                :: msg_executeJob(jv)
                :: msg_responseJob(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("as" -> dispatch))))
    }

    Executors.newFixedThreadPool(1).submit(this)
}

