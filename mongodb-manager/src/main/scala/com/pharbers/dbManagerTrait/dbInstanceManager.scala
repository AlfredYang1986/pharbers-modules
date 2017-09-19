package com.pharbers.dbManagerTrait

import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.moduleConfig.{ConfigDefines, ConfigImpl}
import com.pharbers.mongodbConnect.connection_instance

import scala.xml.Node

trait dbInstanceManager extends PharbersInjectModule {

    override val id: String = "mongodb-connect-nodes"
    override val configPath: String = "pharbers_config/db_manager.xml"
    override val md = "connect-config-path" :: Nil //"server_host" :: "server_port" :: "connect_name" :: "connect_pwd" :: "conn_name" :: Nil

    import com.pharbers.moduleConfig.ModuleConfig.fr
    implicit val f : (ConfigDefines, Node) => ConfigImpl = { (c, n) =>
        ConfigImpl(
            c.md map { x => x -> ((n \ x).toList map { iter =>
                (iter \\ "@name").toString -> new dbInstance((iter \\ "@value").toString)
            })}
        )}
    override lazy val config: ConfigImpl = loadConfig(configDir + "/" + configPath)

    lazy val connections : List[(String, connection_instance)] =
        config.mc.find(p => p._1 == md.head).get._2.asInstanceOf[List[(String, connection_instance)]]

    def queryDBInstance(name : String) : Option[connection_instance] =
        connections.find(p => p._1 == name).map (x => Some(x._2)).getOrElse(None)
}
