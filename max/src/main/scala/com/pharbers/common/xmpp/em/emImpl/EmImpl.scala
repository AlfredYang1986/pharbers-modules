package com.pharbers.common.xmpp.em.emImpl

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by spark on 18-4-26.
  */
trait EmImpl extends EmTrait with EmInstance { this: EmBase =>

    override def registerUser(name: String, pwd: String): JsValue = {
        val args = toJson(
            Map(
                "username" -> toJson(name),
                "password" -> toJson(pwd)
            )
        )
        try{
            proxy("users").post(args)
        } catch {
            case _: Exception => throw new Exception("user email has been use for EM")
        }
    }

    override def deleteUser(name: String): JsValue = {
        try{
            proxy(s"users/$name").delete
        } catch {
            case _: Exception => throw new Exception("user not exist for EM")
        }
    }

    override def disconnectUser(name: String): JsValue = {
        try{
            proxy(s"users/$name/disconnect").get
        } catch {
            case _: Exception => throw new Exception("user not exist for EM")
        }
    }

    override def queryAllChatgroup(): List[(String, String)] = {
        (proxy("chatgroups").get \ "data").as[List[JsValue]]
                .map(jv => jv.as[Map[String, JsValue]])
                .map(m => m("groupname").as[String] -> m("groupid").as[String])
    }

    override def queryGroupIdByName(groupname: String): Option[String] = {
        queryAllChatgroup().find(_._1 == groupname).map(_._2)
    }

    override def createChatgroup(groupname: String, desc: String = "", public: Boolean = true,
                                 maxusers: Int = 200, approval: Boolean = false, owner: String = "pharbers_master"): JsValue = {
        val args = toJson(
            Map(
                "groupname" -> toJson(groupname),
                "desc" -> toJson(desc),
                "public" -> toJson(public),
                "maxusers" -> toJson(maxusers),
                "approval" -> toJson(approval),
                "owner" -> toJson(owner)
            )
        )

        queryGroupIdByName(groupname) match {
            case Some(_) => throw new Exception("group name has been use for EM")
            case None => proxy("chatgroups").post(args)
        }
    }

    override def deleteChatgroup(groupname: String): JsValue = {
        val groupId = queryGroupIdByName(groupname) match {
            case Some(id) => id
            case None => throw new Exception("group not exist for EM")
        }
        proxy(s"chatgroups/$groupId").delete
    }

    override def userJoinChatgroup(userName: String, groupName: String): JsValue = {
        val groupId = queryGroupIdByName(groupName) match {
            case Some(id) => id
            case None => throw new Exception("group not exist for EM")
        }
        try{
            proxy(s"chatgroups/$groupId/users/$userName").post()
        } catch {
            case ex: Exception =>
                if(ex.getMessage.contains("HTTP response code: 403"))
                    throw new Exception("user is already in the group for EM")
                else
                    throw new Exception("user not exist for EM")
        }
    }

    override def userQuitChatgroup(userName: String, groupName: String): JsValue = {
        val groupId = queryGroupIdByName(groupName) match {
            case Some(id) => id
            case None => throw new Exception("group not exist for EM")
        }
        try{
            proxy(s"chatgroups/$groupId/users/$userName").delete
        } catch {
            case _: Exception => throw new Exception("user is not in the group for EM")
        }
    }

    override def sendMessage2User(userName: String, msg: JsValue): Unit = {
        val args = toJson(
            Map(
                "target_type" -> toJson("users"),
                "target" -> toJson(List(toJson(userName))),
                "msg" -> toJson(Map(
                    "type" -> toJson("txt"),
                    "msg" -> msg
                ))
            )
        )

        proxy("messages").post(args)
    }

    override def sendMessage2Group(groupName: String, msg: JsValue): Unit = {
        val groupId = queryGroupIdByName(groupName) match {
            case Some(id) => id
            case None => throw new Exception("group not exist for EM")
        }

        val args = toJson(
            Map(
                "target_type" -> toJson("chatgroups"),
                "target" -> toJson(List(toJson(groupId))),
                "msg" -> toJson(Map(
                    "type" -> toJson("txt"),
                    "msg" -> msg
                ))
            )
        )

        proxy("messages").post(args)
    }

}
