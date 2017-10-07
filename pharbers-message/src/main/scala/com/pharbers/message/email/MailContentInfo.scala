package com.pharbers.message.email

import com.pharbers.message.common.MessageTrait

trait MailContentInfo extends MessageTrait {

	override val id: String = "email-content"
	override val configPath: String = "pharbers_config/email_content.xml"
	override val md: List[String] = "from" :: "pwd" :: "port" ::
									"host" :: "tls" :: "ssl" ::
									"theme" :: "context" :: Nil
	
	lazy val from: String = config.mc.find(_._1 == "from").map(_._2.toString).getOrElse("")
	lazy val pwd: String = config.mc.find(_._1 == "pwd").map(_._2.toString).getOrElse("")
	lazy val port: Int = config.mc.find(_._1 == "port").map(_._2.toString.toInt).getOrElse(0)
	lazy val host: String = config.mc.find(_._1 == "host").map(_._2.toString).getOrElse("")
	lazy val tls: Boolean = config.mc.find(_._1 == "tls").map(_._2.toString.toBoolean).getOrElse(false)
	lazy val ssl: Boolean = config.mc.find(_._1 == "ssl").map(_._2.toString.toBoolean).getOrElse(false)
	lazy val theme: String = config.mc.find(_._1 == "theme").map(_._2.toString).getOrElse("")
	lazy val context: String = config.mc.find(_._1 == "context").map(_._2.toString).getOrElse("")
	override def toString: String =
		s"from => $from,\npwd => $pwd, \n" +
		s"port => $port, \nhost => $host, \n" +
		s"tls => $tls, \nssl => $ssl, \n" +
		s"theme => $theme,\n" +
		s"context => $context"
}