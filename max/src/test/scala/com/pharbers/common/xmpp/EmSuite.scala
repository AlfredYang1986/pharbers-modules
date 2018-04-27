//package com.pharbers.common.xmpp
//
//import org.scalatest.FunSuite
//import play.api.libs.json.Json.toJson
//
///**
//  * Created by clock on 17-9-7.
//  */
//class EmSuite extends FunSuite {
//    val ed: emDriver = emDriver()
//    val testUser: String = "testUser"
//    val testGroup: String = "testGroup"
//
//    test("test em registerUser") {
//        ed.registerUser(testUser, "testPwd")
//    }
//
//    test("test em createChatgroup") {
//        ed.createChatgroup(testGroup)
//    }
//
//    test("test em userJoinChatgroup") {
//        ed.userJoinChatgroup(testUser, testGroup)
//    }
//
//    test("test em sendMessage") {
//        val msg = toJson("test -> ok")
//        ed.sendMessage2User(testUser, msg)
//        ed.sendMessage2Group(testGroup, msg)
//    }
//
//    test("test em userQuitChatgroup") {
//        ed.userQuitChatgroup(testUser, testGroup)
//    }
//
//    test("test em disconnectUser") {
//        ed.disconnectUser(testUser)
//    }
//
//    test("test em deleteUser") {
//        ed.deleteUser(testUser)
//    }
//
//    test("test em deleteChatgroup") {
//        ed.deleteChatgroup(testGroup)
//    }
//
//}
