package com.pharbers.delivery.util

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by jeorch on 18-3-14.
  */
trait ConfigMongo extends PharbersInjectModule {
    override val id: String = "mongodb-connect"
    override val configPath: String = "pharbers_config/calc_connect.xml"
    override val md = "server_host" :: "server_port" :: "connect_name" :: "connect_pwd" :: "conn_name" :: Nil

    val mongodbHost: String = config.mc.find(p => p._1 == "server_host").get._2.toString
    val mongodbPort: String = config.mc.find(p => p._1 == "server_port").get._2.toString
}
