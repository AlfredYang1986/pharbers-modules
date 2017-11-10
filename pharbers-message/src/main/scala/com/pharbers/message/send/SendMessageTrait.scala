package com.pharbers.message.send

import java.util.Date

import com.mongodb.casbah.Imports._
import com.pharbers.cliTraits.DBTrait
import com.pharbers.message.email.MailTrait
import com.pharbers.message.im.EmChatMsg
import com.pharbers.message.websocket.WebSocket
import com.pharbers.sercuity.Sercurity
import org.apache.commons.mail.{HtmlEmail, SimpleEmail}
import play.api.libs.json.Json.toJson

class MessageType(val num: Int, val str: String)
class EmailType(override val num: Int = 0, override val str: String = "Email") extends MessageType(num, str)
case class EmailAuthCodeType() extends EmailType(str = "EmailAuthCode")
case class EmailActiveCodeType() extends EmailType(str = "EmailActiveCode")
case class EmailResetPasswordType() extends EmailType(str = "EmailResetPassword")
case class IMType() extends MessageType(1, "IM")
case class SMSType() extends MessageType(2, "SMS")

case class Mail(addr: String) {
	val mail = new MailTrait(addr)
	
	def sendTextMail: MailTrait = {
		mail.email = Some(new SimpleEmail)
		mail
	}
	
	def sendHtmlMail: MailTrait = {
		mail.email = Some(new HtmlEmail)
		mail
	}
}

trait SendMessageTrait {
	
	def sendMailMessage(address: String, pt: MessageType)(implicit db: DBTrait): Mail = {
		db.queryObject(DBObject("address" -> Sercurity.md5Hash(s"$address${pt.str}")), "send_message") { obj =>
			val reg_id = obj.getAs[String]("reg_id")
			Map("reg_id" -> toJson(reg_id))
		} match {
			case None => db.insertObject(DBObject("reg_id" -> Sercurity.md5Hash(s"$address${new Date().getTime}"), "address" -> Sercurity.md5Hash(s"$address${pt.str}")), "send_message", "reg_id")
			case Some(x) =>
				val reVal = x.get("reg_id").get.as[String]
				if(Sercurity.getTimeSpanWithPast10Minutes.map(x => Sercurity.md5Hash(s"$address$x")).contains(reVal)) throw new Exception("please try in 10 minutes")
				else db.updateObject(DBObject("reg_id" -> Sercurity.md5Hash(s"$address${new Date().getTime}"), "address" -> Sercurity.md5Hash(s"$address${pt.str}")), "send_message", "address")
		}
		Mail(address)
	}
	
	def sendImMessage = EmChatMsg()
	
	def wSocket = WebSocket()
	
//	def sendSMSMessage = ???
}