package com.pharbers.common.algorithm

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by jeorch on 18-3-28.
  */
object max_data_sync_mongo_obj extends PharbersInjectModule {
    override val id: String = "mongodb-connect"
    override val configPath: String = "pharbers_config/db_data_connect.xml"
    override val md = "server_host" :: "server_port" :: "connect_name" :: "connect_pwd" :: "conn_name" :: Nil

    val mongodbHost: String = config.mc.find(p => p._1 == "server_host").get._2.toString
    val mongodbPort: String = config.mc.find(p => p._1 == "server_port").get._2.toString
    val databaseName: String = config.mc.find(p => p._1 == "conn_name").get._2.toString

}
