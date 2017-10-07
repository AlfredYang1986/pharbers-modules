package com.pharbers.message.im

import com.pharbers.message.common.MessageTrait

trait InstantMessageInfo extends MessageTrait {
	
	override val id: String = "im-content"
	override val configPath: String = "pharbers_config/im_content.xml"
	override val md: List[String] = "org_name" :: "app_name" :: "grant_type" ::
									"client_id" :: "client_secret" :: Nil
	
	lazy val AppKey: String = config.mc.find(_._1 == "org_name").map(_._2.toString).getOrElse("")
	lazy val AppName: String = config.mc.find(_._1 == "app_name").map(_._2.toString).getOrElse("")
	lazy val AppSecret: String = config.mc.find(_._1 == "client_secret").map(_._2.toString).getOrElse("")
	lazy val GrantType: String = config.mc.find(_._1 == "grant_type").map(_._2.toString).getOrElse("")
	lazy val ClientId: String = config.mc.find(_._1 == "client_id").map(_._2.toString).getOrElse("")

	override def toString: String =
		s"AppKey => $AppKey, \n"+
		s"AppName => $AppName, \n"+
		s"AppSecret => $AppSecret, \n"+
		s"GrantType => $GrantType, \n"+
		s"ClientId => $ClientId"
}