package com.pharbers.message.im

import com.pharbers.message.common.MessageTrait
import com.pharbers.moduleConfig.{ConfigDefines, ConfigImpl}

import scala.xml.Node

trait BaseTrait extends MessageTrait {
	override val id: String = "message-content-nodes"
	override val configPath: String = "pharbers_config/message_manager.xml"
	override val md: List[String] = "content-config-path" :: Nil
	
	import com.pharbers.moduleConfig.ModuleConfig.fr
	override lazy val config: ConfigImpl = loadConfig(configDir + "/" + configPath)
	
	implicit val format : (ConfigDefines, Node) => ConfigImpl = { (c, n) =>
		ConfigImpl(
			c.md map { x => x -> ((n \ x).toList map { iter =>
				(iter \\ "@name").toString -> new InstantMessageInstance((iter \\ "@value").toString)
			})}
		)
	}
}

trait InstantMessageTrait extends BaseTrait {
	
	def sendMsg(msg: String): String
	
//	def createUser(account: String, nickName: Option[String], pwd: String): String = ???
//	def updateUser(nickName: Option[String], pwd: Option[String]): String = ???
//	def banUser(account: String): String = ???
}
