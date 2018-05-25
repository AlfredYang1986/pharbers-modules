package com.pharbers.common.xmpp.em.emImpl

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by spark on 18-4-26.
  */
trait EmBase extends PharbersInjectModule {
    override val id: String = "im-content"
    override val configPath: String = "pharbers_config/im-content.xml"
    override val md = "host" :: "org_name" :: "app_name" :: "client_id" :: "client_secret" :: Nil

    lazy val host: String = config.mc.find(p => p._1 == "host").get._2.toString
    lazy val org_name: String = config.mc.find(p => p._1 == "org_name").get._2.toString
    lazy val app_name: String = config.mc.find(p => p._1 == "app_name").get._2.toString
    lazy val client_id: String = config.mc.find(p => p._1 == "client_id").get._2.toString
    lazy val client_secret: String = config.mc.find(p => p._1 == "client_secret").get._2.toString
    val emUrl: String = host + "/" + org_name + "/" + app_name + "/"
}