package com.pharbers.channel.chanelImpl

import java.util.Date
import play.api.libs.json.JsValue
import com.pharbers.common.xmpp.kafka._

case class responsePusher() extends kafkaBasicConf with kafkaPushRecord {

    override lazy val endpoints: String = kafka_config_obj.endpoints
    override lazy val schemapath: String = kafka_config_obj.responseSP
    override lazy val topic: String = kafka_config_obj.callJobResponseTopic

    def callJobResponse(result: Map[String, String], stage: String)
                       (jv: JsValue): Unit = {

        val mapResult = new java.util.HashMap[String, Object]()
        result.foreach { x =>
            mapResult.put(x._1, x._2)
        }

        val msg = Map(
            "user_id" -> (jv \ "user_id").asOpt[String].get,
            "company_id" -> (jv \ "company_id").asOpt[String].get,
            "date" -> new Date().getTime.toString,
            "call" -> (jv \ "call").asOpt[String].get,
            "stage" -> stage,
            "result" -> mapResult
        )

        this.pushRecord(msg)(this.precord)
    }
}

