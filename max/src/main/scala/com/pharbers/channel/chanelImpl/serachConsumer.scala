package com.pharbers.channel.chanelImpl

import akka.actor.ActorSystem
import play.api.libs.json.JsValue
import java.util.concurrent.Executors
import play.api.libs.json.Json.toJson
import com.pharbers.common.xmpp.kafka._
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.channel.searchRequestMessage.{msg_search, msg_searchResponse}

case class serachConsumer(override val group_id: String)
                         (override implicit val dispatch: ActorSystem)
        extends kafkaBasicConf with kafkaConsumer {

    override lazy val endpoints: String = kafka_config_obj.endpoints
    override lazy val schemapath: String = kafka_config_obj.requestSP
    override lazy val topic: String = kafka_config_obj.callJobTopic

    override val consumeHandler: JsValue => MessageRoutes = { jv =>
        import com.pharbers.bmpattern.LogMessage.common_log
        import com.pharbers.bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("search request"))), jv)
                :: msg_search(jv)
                :: msg_searchResponse(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("as" -> dispatch))))
    }

    Executors.newFixedThreadPool(1).submit(this)
}

