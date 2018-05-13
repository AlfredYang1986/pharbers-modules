package com.pharbers.channel.chanelImpl

import java.util.Date
import com.pharbers.common.xmpp.kafka._
import play.api.libs.json.{JsString, JsValue}

case class responsePusher() extends kafkaBasicConf with kafkaPushRecord {

    override lazy val endpoints: String = kafka_config_obj.endpoints
    override lazy val schemapath: String = kafka_config_obj.progressSP
    override lazy val topic: String = kafka_config_obj.progressTopic

    def callJobResponse(result: JsValue, stage: String)
                       (jv: JsValue): Unit = {
        val msg = Map(
            "job_id" -> (jv \ "job_id").asOpt[String].get,
            "user_id" -> (jv \ "user_id").asOpt[String].get,
            "company_id" -> (jv \ "company_id").asOpt[String].get,
            "date" -> new Date().getTime.toString,
            "call" -> (jv \ "call").asOpt[String].get,
            "stage" -> stage,
            "result" -> result.as[JsString].value
        )

        this.pushRecord(msg)(this.precord)
    }
}

