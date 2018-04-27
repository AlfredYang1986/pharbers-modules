package com.pharbers.channel.chanelImpl

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by spark on 18-4-24.
  */
object kafka_config_obj extends PharbersInjectModule  {

    override val id: String = "progress-kafka-config"
    override val configPath: String = "pharbers_config/pharbers-kafka-config.xml"
    override val md: List[String] = "endpoints" :: "callJobSP" :: "callJobTopic" ::
            "progressSP" :: "progressTopic" :: Nil

    val endpoints: String = config.mc.find(p => p._1 == "endpoints").get._2.toString
    val callJobSP: String = config.mc.find(p => p._1 == "callJobSP").get._2.toString
    val callJobTopic: String = config.mc.find(p => p._1 == "callJobTopic").get._2.toString
    val progressSP: String = config.mc.find(p => p._1 == "progressSP").get._2.toString
    val progressTopic: String = config.mc.find(p => p._1 == "progressTopic").get._2.toString

}
