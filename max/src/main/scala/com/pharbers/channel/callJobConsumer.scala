package com.pharbers.channel

import akka.actor.ActorSystem
import play.api.libs.json.JsValue
import java.util.concurrent.Executors
import play.api.libs.json.Json.toJson
import com.pharbers.channel.chanelImpl._
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.main.callJobRequest.callJobRequestMessage._
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage


case class callJobConsumer(override val group_id: String)
                          (override implicit val dispatch: ActorSystem)
        extends kafkaBasicConf with kafkaConsumer {

    override lazy val endpoints: String = kafka_config_obj.endpoints
    override lazy val schemapath: String = kafka_config_obj.callJobSP
    override lazy val topic: String = kafka_config_obj.callJobTopic

    override val consumeHandler: JsValue => MessageRoutes = { jv =>
        import com.pharbers.bmpattern.LogMessage.common_log
        import com.pharbers.bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("call job request"))), jv)
                :: msg_choiceJob(jv)
                :: msg_executeJob(jv)
                :: msg_responseJob(jv)
                :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("as" -> dispatch))))
    }

    Executors.newFixedThreadPool(1).submit(this)
}

