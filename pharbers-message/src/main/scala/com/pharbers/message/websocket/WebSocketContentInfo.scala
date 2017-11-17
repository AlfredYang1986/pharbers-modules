package com.pharbers.message.websocket

import com.pharbers.message.common.MessageTrait

trait WebSocketContentInfo extends MessageTrait {
	override val id: String = "wsocket-content"
	override val configPath: String = "pharbers_config/wsocket_content.xml"
	override val md: List[String] = "remote_connect" :: "local_connect" :: Nil
	
	lazy val remoteConnect: String = config.mc.find(_._1 == "remote_connect").map(_._2.toString).getOrElse("")
	lazy val localConnect: String = config.mc.find(_._1 == "local_connect").map(_._2.toString).getOrElse("")
	
	override def toString: String =
		s"remoteConnect => $remoteConnect,\nlocalConnect => $localConnect \n"
}
