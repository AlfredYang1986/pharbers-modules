package com.pharbers.common.xmpp.kafka

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by spark on 18-4-24.
  */
object kafka_config_obj extends PharbersInjectModule {

    override val id: String = "progress-kafka-config"
    override val configPath: String = "pharbers_config/pharbers-kafka-config.xml"
    override val md: List[String] = "endpoints" :: "requestSP" :: "responseSP" ::
            "callJobTopic" :: "callJobResponseTopic" ::
            "searchTopic" :: "searchResponseTopic" :: Nil

    val endpoints: String = config.mc.find(p => p._1 == "endpoints").get._2.toString
    val requestSP: String = config.mc.find(p => p._1 == "requestSP").get._2.toString
    val responseSP: String = config.mc.find(p => p._1 == "responseSP").get._2.toString
    val callJobTopic: String = config.mc.find(p => p._1 == "callJobTopic").get._2.toString
    val callJobResponseTopic: String = config.mc.find(p => p._1 == "callJobResponseTopic").get._2.toString
    val searchTopic: String = config.mc.find(p => p._1 == "searchTopic").get._2.toString
    val searchResponseTopic: String = config.mc.find(p => p._1 == "searchResponseTopic").get._2.toString

}
