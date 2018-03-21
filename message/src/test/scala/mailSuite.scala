//import java.util.Date
//
//import com.pharbers.cliTraits.DBTrait
//import com.pharbers.dbManagerTrait.dbInstanceManager
//import com.pharbers.message.email.MailTrait
//import com.pharbers.message.im.EmChatMsg
//import com.pharbers.message.send.SendMessageTrait
//import org.scalatest.FunSuite
//import play.api.libs.json.{JsValue, Json}
//
//class mailSuite extends FunSuite {
////	object temp extends SendMessageTrait with dbInstanceManager
////	implicit val db: DBTrait = temp.queryDBInstance("cli").get
//	test("EmChatRoom Test") {
////	    temp.sendMailMessage("pqian@pharbers.com").sendTextMail.setSubTheme("test").setContext("<h1><p style='color:red'>test</p><h1>").sendToEmail
////	    temp.sendTextMail.setSubTheme("test").setContext("<h1><p style='color:red'>test</p><h1>").sendToEmail("pqian@pharbers.com")
////	    temp.sendHtmlMail.setSubTheme("test").setContext("<h1><p style='color:red'>test</p><h1>").sendToEmail("pqian@pharbers.com")
//
////	    val str = EmChatMsg().sendFromUser("project").sendTargetUser("31593474359297" :: Nil).sendTargetType("chatrooms").sendMsgContentType().sendMsgExt(Map("进度条" -> "10")).sendMsg("fuck")
////		val str = EmChatMsg().setRoomName("bbb").setRoomDescription("bbb").setRoomOnwer("project").setRoomMaxUsers(100).createChatRoom
////		val str = EmChatMsg().setRoomMembers("31689742024705", "71af921e5793360df771351bf878eaab" :: Nil)
//
////		val str = EmChatMsg().getAllRooms
////		val str = EmChatMsg().getRoomDetail("31577339920385")
////		val str = EmChatMsg().deleteChatRoom("31577339920385")
////		val str = EmChatMsg().
////		val str = EmChatMsg().deleteRegisterUser("Faiz")
////		val str = EmChatMsg().getUsersBatch()
////
//		val roomName = s"fea9f203d4f593a96f0d6faa91ba24ba_47ee6f05c8994e9ddbe12c2971600788"
//		val reVal = (Json.parse(EmChatMsg()
//				.setRoomName(roomName)
//				.setRoomDescription(roomName)
//				.setRoomOnwer("project")
//				.setRoomMaxUsers(200)
//				.createChatRoom) \ "data").as[String Map JsValue]
//
//		val roomId = reVal("id").as[String]
//
//		(Json.parse(EmChatMsg().getUsersBatch()) \ "entities").as[List[String Map JsValue]].filter(x =>
//			x("username").as[String].indexOf("fea9f203d4f593a96f0d6faa91ba2400ba_") != -1 && x("username").as[String].indexOf("_47ee6f05c8994e9ddbe12c2971600788") != -1
//		).map(x => x("username").as[String]) match {
//			case Nil => Unit
//			case lst => EmChatMsg().setRoomMembers(roomId, lst)
//		}
//
//
//		//fea9f203d4f593a96f0d6faa91ba24ba_55555555555555555555555555555_47ee6f05c8994e9ddbe12c2971600788
//		//fea9f203d4f593a96f0d6faa91ba24ba_47ee6f05c8994e9ddbe12c2971600788
//
////	val reVal = (Json.parse(EmChatMsg().getAllRooms) \ "data").as[List[String Map JsValue]]
////		.filterNot(x => x("name").as[String].split("_").head != "fea9f203d4f593a96f0d6faa91ba24ba")
////		.map(x => x("id").as[String])
////
////	val str = EmChatMsg().sendFromUser("project")
////		.sendTargetUser(reVal)
////		.sendTargetType("chatrooms")
////		.sendMsgContentType()
////		.sendMsgExt(Map("uuid" -> "0120", "company" -> "fea9f203d4f593a96f0d6faa91ba24ba", "type" -> "progress_calc"))
////		.sendMsg("100")
////
////		println(str)
//
////		"fea9f203d4f593a96f0d6faa91ba24ba_9fa3b571d612362b31a385aee7a60537_47ee6f05c8994e9ddbe12c2971600766"
//
//	}
//}