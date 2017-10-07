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

sealed trait MailTrait[T] extends BaseTrait{
	val mailConf: MailContentInfo = queryMessageInstance("email").asInstanceOf[MailContentInfo]
	val email: Email
	def setSubTheme(sub: String): T
	def setContext(cont: String): T
	def sendToEmail(address: String): String = {
		email.setHostName(mailConf.host)
		email.setSSLOnConnect(mailConf.ssl)
		email.setAuthentication(mailConf.from, mailConf.pwd)
		email.setSmtpPort(mailConf.port)
		email.setFrom(mailConf.from)
		email.addTo(address)
		email.setCharset("UTF-8")
		email.send()
	}
}

case class Mail() {
	def textMail = TextMail()
	def htmlMail = HtmlMail()
}

protected case class TextMail() extends MailTrait[TextMail] {
	override val email: Email = new SimpleEmail
	
	override def setContext(cont: String = mailConf.context): TextMail = {
		email.setMsg(cont)
		this
	}

	override def setSubTheme(sub: String = mailConf.theme): TextMail = {
		email.setSubject(sub)
		this
	}
}

protected case class HtmlMail() extends MailTrait[HtmlMail] {
	override val email: HtmlEmail = new HtmlEmail
	
	override def setContext(cont: String): HtmlMail = {
		email.setHtmlMsg(cont)
		this
	}
	
	override def setSubTheme(sub: String = mailConf.theme): HtmlMail = {
		email.setSubject(sub)
		this
	}
}