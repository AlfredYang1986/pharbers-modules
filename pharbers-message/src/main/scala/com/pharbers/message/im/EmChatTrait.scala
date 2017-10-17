package com.pharbers.message.im

import io.swagger.client.api.{AuthenticationApi, MessagesApi}
import io.swagger.client.model.{Msg, MsgContent, Token, UserName}
import play.api.libs.json.{JsValue, Json}

sealed trait EmChatTrait extends InstantMessageTrait {
	object access {var flag: Map[String, Any] = Map.empty}
	protected val userList: UserName
	protected val msgObj: Msg
	protected val msgContentObj: MsgContent
	protected val imConf: InstantMessageInfo = queryMessageInstance("im").asInstanceOf[InstantMessageInfo]
	
	override def sendMsg(msg: String): String = {
		val reVal = msgObj.msg(msgContentObj.msg(msg))
		invokeEasemobAPI(reVal)
	}
	
	protected def invokeEasemobAPI(payload: Msg): String = {
		new MessagesApi().orgNameAppNameMessagesPost(imConf.AppKey, imConf.AppName, getAccessToken, payload)
	}
	protected def getAccessToken: String = {

		def initTokenByProp: Map[String, Any] = {
			val map = Json.parse(new AuthenticationApi().orgNameAppNameTokenPost(imConf.AppKey, imConf.AppName,
				new Token().
					clientId(imConf.ClientId).
					grantType(imConf.GrantType).
					clientSecret(imConf.AppSecret))).as[Map[String, JsValue]]
			Map("access_token" -> (" Bearer " + map.get("access_token").map(x => x).get.asOpt[String].getOrElse("")),
				"expirdat" -> (System.currentTimeMillis() + map.get("expires_in").map(x => x.as[Double]).getOrElse(-1D)))
		}

		if(access.flag.isEmpty || System.currentTimeMillis() > access.flag.get("expirdat").map(x => x.toString.toDouble).getOrElse(0D))
			access.flag = initTokenByProp
		access.flag.get("access_token").map(x => x.toString).getOrElse("")
	}
}

case class EmChatMsg() extends EmChatTrait {
	protected override val userList: UserName = new UserName
	protected override val msgObj: Msg = new Msg
	protected override val msgContentObj: MsgContent = new MsgContent
	
	def sendTargetUser(userNameLst: List[String]): EmChatMsg = {
		userNameLst foreach (x => userList.add(x))
		msgObj.target(userList)
		this
	}
	
	def sendFromUser(userName: String): EmChatMsg = {
		msgObj.from(userName)
		this
	}
	
	def sendTargetType(t: String): EmChatMsg = {
		msgObj.setTargetType(t)
		this
	}
	
	/**
	  * 信息类型
	  * @param enum 默认值为TXT文本类型
	  * @return EmChatMsg this
	  */
	def sendMsgContentType(enum: MsgContent.TypeEnum = MsgContent.TypeEnum.TXT): EmChatMsg = {
		msgContentObj.`type`(enum)
		this
	}
	
	def sendMsgExt(ext: String Map String): EmChatMsg = {
		msgObj.setExt(ext)
		this
	}
}