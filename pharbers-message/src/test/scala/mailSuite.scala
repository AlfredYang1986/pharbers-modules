import com.pharbers.cliTraits.DBTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.message.email.MailTrait
import com.pharbers.message.im.EmChatMsg
import com.pharbers.message.send.SendMessageTrait
import org.scalatest.FunSuite

class mailSuite extends FunSuite {
//	object temp extends SendMessageTrait with dbInstanceManager
//	implicit val db: DBTrait = temp.queryDBInstance("cli").get
//	test("EmChatRoom Test") {
//	    temp.sendMailMessage("pqian@pharbers.com").sendTextMail.setSubTheme("test").setContext("<h1><p style='color:red'>test</p><h1>").sendToEmail
//	    temp.sendTextMail.setSubTheme("test").setContext("<h1><p style='color:red'>test</p><h1>").sendToEmail("pqian@pharbers.com")
//	    temp.sendHtmlMail.setSubTheme("test").setContext("<h1><p style='color:red'>test</p><h1>").sendToEmail("pqian@pharbers.com")
//	    EmChatMsg().sendFromUser("project").sendTargetUser("test" :: Nil).sendTargetType("users").sendMsgContentType().sendMsgExt(Map("进度条" -> "10")).sendMsg("fuck")
//		val str = EmChatMsg().setRoomName("TestCalc").setRoomDescription("TestCalcDes").setRoomOnwer("project").setRoomMaxUsers(100).createChatRoom
//		val str = EmChatMsg().setRoomMembers("31577339920385", "test" :: Nil)
//		val str = EmChatMsg().getAllRooms
//		val str = EmChatMsg().getRoomDetail("31577339920385")
//		val str = EmChatMsg().deleteChatRoom("31577339920385")
//		val str = EmChatMsg().
//		val str = EmChatMsg().deleteRegisterUser("Faiz")
//		println(str)
//	}
}