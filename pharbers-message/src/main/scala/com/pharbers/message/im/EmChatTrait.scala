package com.pharbers.message.im

import io.swagger.client.api.{AuthenticationApi, ChatRoomsApi, MessagesApi, UsersApi}
import io.swagger.client.model._
import play.api.libs.json.{JsValue, Json}

sealed trait EmChatTrait extends InstantMessageTrait {
	object access {var flag: Map[String, Any] = Map.empty}
	protected val msgObj: Msg
	protected val roomObj: Chatroom
	protected val msgContentObj: MsgContent
	protected val imConf: InstantMessageInfo = queryMessageInstance("im").asInstanceOf[InstantMessageInfo]
	
	override def sendMsg(msg: String): String = {
		val reVal = msgObj.msg(msgContentObj.msg(msg))
		new MessagesApi().orgNameAppNameMessagesPost(imConf.AppKey, imConf.AppName, getAccessToken, reVal)
	}
	
	override def createChatRoom: String =
		new ChatRoomsApi().orgNameAppNameChatroomsPost(imConf.AppKey, imConf.AppName, getAccessToken, roomObj)
	
	override def deleteChatRoom(roomId: String): String =
		new ChatRoomsApi().orgNameAppNameChatroomsChatroomIdDelete(imConf.AppKey, imConf.AppName, getAccessToken, roomId)
	
	override def setRoomMembers(roomId: String, userList: List[String]): String = {
		val users = new UserName
		val userLst = new UserNames
		userList foreach { x =>
			users.add(x)
			userLst.usernames(users)
		}
		new ChatRoomsApi().orgNameAppNameChatroomsChatroomIdUsersPost(imConf.AppKey, imConf.AppName, getAccessToken, roomId, userLst)
	}
	
	override def getAllRooms: String =
		new ChatRoomsApi().orgNameAppNameChatroomsGet(imConf.AppKey, imConf.AppName, getAccessToken)
	
	override def getRoomDetail(roomId: String): String =
		new ChatRoomsApi().orgNameAppNameChatroomsChatroomIdGet(imConf.AppKey, imConf.AppName, getAccessToken, roomId)
	
	override def getUsersBatch(limit: Long = 1000, cursor: String = null): String = {
		new UsersApi().orgNameAppNameUsersGet(imConf.AppKey, imConf.AppName, getAccessToken, limit.toString, cursor)
	}
	
	override def registerUser(name: String, pwd: String): String = {
		val users = new RegisterUsers()
		val user = new User().username(name).password(pwd)
		users.add(user)
		new UsersApi().orgNameAppNameUsersPost(imConf.AppKey, imConf.AppName, users, getAccessToken)
	}
	
	override def deleteRegisterUser(name: String): String = new UsersApi().orgNameAppNameUsersUsernameDelete(imConf.AppKey, imConf.AppName, getAccessToken, name)
	
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
	protected override val msgObj: Msg = new Msg
	protected override val roomObj: Chatroom = new Chatroom
	protected override val msgContentObj: MsgContent = new MsgContent
	
	def sendTargetUser(userNameLst: List[String]): EmChatMsg = {
		val userList: UserName = new UserName
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
	
	def sendMsgContentType(enum: MsgContent.TypeEnum = MsgContent.TypeEnum.TXT): EmChatMsg = {
		msgContentObj.`type`(enum)
		this
	}
	
	def sendMsgExt(ext: String Map String): EmChatMsg = {
		msgObj.setExt(ext)
		this
	}
	
	def setRoomName(name: String): EmChatMsg = {
		roomObj.setName(name)
		this
	}
	
	def setRoomDescription(context: String): EmChatMsg = {
		roomObj.setDescription(context)
		this
	}
	
	def setRoomMaxUsers(num: Int): EmChatMsg = {
		roomObj.setMaxusers(num)
		this
	}
	
	def setRoomOnwer(name: String): EmChatMsg = {
		roomObj.setOwner(name)
		this
	}
}