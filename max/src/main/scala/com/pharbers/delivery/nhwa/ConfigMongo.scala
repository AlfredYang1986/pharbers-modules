package com.pharbers.delivery.nhwa

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by jeorch on 18-3-14.
  */
trait ConfigMongo extends PharbersInjectModule {
    override val id: String = "mongo-config"
    override val configPath: String = "pharbers_config/mongo-config.xml"
    override val md =
        "host" :: "port" :: Nil

    val mongoHost: String = config.mc.find(p => p._1 == "host").get._2.toString
    val mongoPort: String = config.mc.find(p => p._1 == "port").get._2.toString
}
