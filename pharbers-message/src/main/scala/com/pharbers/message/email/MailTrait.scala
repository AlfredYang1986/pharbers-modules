package com.pharbers.message.email

import com.pharbers.message.common.MessageTrait
import com.pharbers.moduleConfig.{ConfigDefines, ConfigImpl}
import org.apache.commons.mail.{Email, HtmlEmail, SimpleEmail}
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
				(iter \\ "@name").toString -> new MailInstance((iter \\ "@value").toString)
			})}
		)
	}
}

sealed class MailTrait(addr: String) extends BaseTrait{
	var email: Option[Email] = None
	val mailConf: MailContentInfo = queryMessageInstance("email").asInstanceOf[MailContentInfo]
	val address: String = addr
	def setSubTheme(sub: String): MailTrait = {
		email.get.setSubject(sub)
		this
	}
	def setContext(cont: String): MailTrait = {
		if(email.get.isInstanceOf[HtmlEmail]) email.get.asInstanceOf[HtmlEmail].setHtmlMsg(cont)
		else email.get.setMsg(cont)
		this
	}
	def sendToEmail: String = {
		email.get.setHostName(mailConf.host)
		email.get.setSSLOnConnect(mailConf.ssl)
		email.get.setAuthentication(mailConf.from, mailConf.pwd)
		email.get.setSmtpPort(mailConf.port)
		email.get.setFrom(mailConf.from)
		email.get.addTo(address)
		email.get.setCharset("UTF-8")
		email.get.send()
	}
	
}
