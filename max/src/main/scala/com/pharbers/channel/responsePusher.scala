package com.pharbers.channel

import java.util.Date

import play.api.libs.json.{JsString, JsValue}
import com.pharbers.channel.chanelImpl._

case class responsePusher() extends kafkaBasicConf with kafkaPushRecord {

    override lazy val endpoints: String = kafka_config_obj.endpoints
    override lazy val schemapath: String = kafka_config_obj.progressSP
    override lazy val topic: String = kafka_config_obj.progressTopic

    def callJobResponse(result: JsValue, stage: String, progress: Int = 0)
                       (jv: JsValue): Unit = {
        val msg = Map(
            "job_id" -> (jv \ "job_id").asOpt[String].get,
            "user_id" -> (jv \ "user_id").asOpt[String].get,
            "company_id" -> (jv \ "company_id").asOpt[String].get,
            "date" -> new Date().getTime.toString,
            "call" -> (jv \ "call").asOpt[String].get,
            "stage" -> stage,
            "progress" -> progress.asInstanceOf[java.lang.Integer],
            "result" -> result.as[JsString].value
        )

        this.pushRecord(msg)(this.precord)
    }
}

