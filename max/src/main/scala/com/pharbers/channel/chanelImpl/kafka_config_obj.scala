package com.pharbers.channel.chanelImpl

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by spark on 18-4-24.
  */
object kafka_config_obj extends PharbersInjectModule  {

    override val id: String = "progress-channel-config"
    override val configPath: String = "pharbers_config/pharbers-channel-config.xml"
    override val md: List[String] = "endpoints" :: "schemapath" :: "topic" :: Nil

    lazy val endpoints: String = config.mc.find(p => p._1 == "endpoints").get._2.toString
    lazy val schemapath: String = config.mc.find(p => p._1 == "schemapath").get._2.toString
    lazy val topic: String = config.mc.find(p => p._1 == "topic").get._2.toString
}
