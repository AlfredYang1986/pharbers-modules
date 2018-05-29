package com.pharbers.common.xmpp.em.emImpl

import play.api.libs.json.JsValue

/**
  * Created by spark on 18-4-26.
  */
trait EmTrait {

    // user manager
    def registerUser(name: String, pwd: String): JsValue
    def deleteUser(name: String): JsValue
    def disconnectUser(name: String): JsValue
    def modifyPossword(name: String, newPwd: String): JsValue = ???

    // chatgroup manager
    def queryAllChatgroup(): List[(String, String)]
    def queryGroupIdByName(name: String): Option[String]
    def createChatgroup(groupname: String, desc: String, public: Boolean,
                        maxusers: Int, approval: Boolean, owner: String): JsValue
    def deleteChatgroup(groupname: String): JsValue

    // user and chatgroup relation
    def userJoinChatgroup(userName: String, groupName: String): JsValue
    def userQuitChatgroup(userName: String, groupName: String): JsValue

    // send Message
    def sendMessage2User(userName: String, msg: JsValue): Unit
    def sendMessage2Group(groupName: String, msg: JsValue): Unit

}
