package com.pharbers.spark.session

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 18-2-26.
  */
trait spark_conn_config extends PharbersInjectModule {
    override val id: String = "spark-config"
    override val configPath: String = "pharbers_config/spark-config.xml"
    override val md = "host" :: "port" :: "name" ::  Nil

    protected val sparkMasterHost: String = config.mc.find(p => p._1 == "host").get._2.toString
    protected val sparkMasterPort: String = config.mc.find(p => p._1 == "port").get._2.toString
    protected val sparkMasterName: String = config.mc.find(p => p._1 == "name").get._2.toString
}
