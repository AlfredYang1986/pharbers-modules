package com.pharbers.xmpp.em

import java.util.Date

import com.pharbers.http._
import com.pharbers.mongodbConnect.from
import com.pharbers.mongodbDriver.MongoDB._data_connection
import com.mongodb.casbah.Imports._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

//object EMNotification {
//    val app_key = "blackmirror#dongda"
//    val org_name = "blackmirror"
//    val app_name = "dongda"
//
//    val client_id = "YXA6A-uzcDn4Eeag6S9CRYv19Q"
//    val client_secret = "YXA6cXkx9z9LnMdNWHRrcNugX4-M354"
//
//    val notification_account = "dongda_master"
//    val notification_password = "PassW0rd"
//
//    val em_host = "https://a1.easemob.com/"
//}

case class EMNotification(val app_key : String,
                          val org_name : String,
                          val app_name : String,
                          val client_id : String,
                          val client_secret : String,
                          val notification_account : String,
                          val notification_password : String,
                          val em_host : String) {

		implicit val dc = _data_connection

	  def getAuthTokenForEM : String = {
  	 
  	    def getAuthTokenFromServer : JsValue = 
			      (HTTP(em_host + org_name + "/" + app_name + "/token").header("Accept" -> "application/json", "Content-Type" -> "application/json").
				        post("grant_type" -> toJson("client_credentials"), "client_id" -> toJson(client_id), "client_secret" -> toJson(client_secret)))
  	 
		    def JS2DBObject(data : JsValue) : Option[MongoDBObject] = {
  	        try {
      	        val builder = MongoDBObject.newBuilder
  	            builder += "access_token" -> (data \ "access_token").asOpt[String].get
  	            builder += "expires_in" -> (data \ "expires_in").asOpt[Long].get * 1000
  	            builder += "date" -> new Date().getTime 
  	            builder += "indentify" -> "em"
  	            
  	            Some(builder.result)
  	            
  	        } catch {
  	          case ex : Exception => None
  	        }
  	    }
  	    
  	    def isTokenValidate(cur : MongoDBObject) : Boolean =
  	        try {
      	        new Date().getTime < cur.getAs[Number]("date").get.longValue + cur.getAs[Number]("expires_in").get.longValue()
  	          
  	        } catch {
  	          case ex : Exception => false
  	        }
				        
  	    (from db() in "em_token" where ("indentify" -> "em") select (x => x)).toList match {
  	      case Nil => {
  	          val d = getAuthTokenFromServer
  	          _data_connection.getCollection("em_token") += JS2DBObject(d).get
  	          (d \ "access_token").asOpt[String].get
  	      }
  	      case head :: Nil => {
  	         if (isTokenValidate(head)) head.getAs[String]("access_token").get
  	         else {
    	          val d = getAuthTokenFromServer
    	          _data_connection.getCollection("em_token").update(DBObject("indentify" -> "em"), JS2DBObject(d).get)
    	          (d \ "access_token").asOpt[String].get
  	         }
  	      }
  	    }
	  }
  	
  	def createChatRoom(pm : Map[String, JsValue]) : JsValue= {
  	    val name = pm.get("groupName").map (x => x.asOpt[String].get).getOrElse("")
  	  
    		var pushMsg : Map[String, JsValue] = Map.empty
    		pushMsg += "name" -> toJson(name)
    		pushMsg += "description" -> toJson(name)
    		pushMsg += "owner" -> toJson("dongda_master")
    	
    		HTTP(em_host + org_name + "/" + app_name + "/chatrooms").header("Accept" -> "application/json", "Content-Type" -> "application/json", "Authorization" -> ("Bearer " + getAuthTokenForEM)).post(toJson(pushMsg)) 
  	}	
  	
  	def createChatGroup(pm : Map[String, JsValue]) : JsValue = {
  	    val name = pm.get("groupName").map (x => x.asOpt[String].get).getOrElse("")
  	  
    		var pushMsg : Map[String, JsValue] = Map.empty
    		pushMsg += "groupname" -> toJson(name)
    		pushMsg += "desc" -> toJson(name)
    		pushMsg += "public" -> toJson(true)
    		pushMsg += "approval" -> toJson(false)
    		pushMsg += "owner" -> toJson("dongda_master")
	
    		HTTP(em_host + org_name + "/" + app_name + "/chatgroups").header("Accept" -> "application/json", "Content-Type" -> "application/json", "Authorization" -> ("Bearer " + getAuthTokenForEM)).post(toJson(pushMsg)) 
	  }
  	
  	def dismissChatGroup(pm : Map[String, JsValue]) : JsValue = {
   	    val group_id = pm.get("groupId").map (x => x.asOpt[String].get).getOrElse("")
  	  
    		HTTP(em_host + org_name + "/" + app_name + "/chatgroups/" + group_id).header("Accept" -> "application/json", "Authorization" -> ("Bearer " + getAuthTokenForEM)).delete
  	}
  	
  	def nofity(pm : Map[String, JsValue]) = {
    		var pushMsg = pm
    		(HTTP(em_host + org_name + "/" + app_name + "/messages").header("Accept" -> "application/json", "Content-Type" -> "application/json", "Authorization" -> ("Bearer " + getAuthTokenForEM)).
    		  		post(toJson(pushMsg)) \ "error").asOpt[String].map (x => println("notification sent error %s", x)).getOrElse(println("notification sent success"))
  	}
  	
  	def registerUser(pm : Map[String, JsValue]) : JsValue = {
  		try {
	  		var pushMsg = pm
	    	HTTP(em_host + org_name + "/" + app_name + "/users").header("Accept" -> "application/json", "Content-Type" -> "application/json", "Authorization" -> ("Bearer " + getAuthTokenForEM)).post(toJson(pushMsg))
  		} catch {
  			case ex : Exception => toJson(Map("error" -> "user existing"))
  		}
  	}

	def forceOffline(user_id : String) : JsValue = {
		try {
			HTTP(em_host + org_name + "/" + app_name + "/users/" + user_id + "/disconnect")
				.header("Accept" -> "application/json",
					"Content-Type" -> "application/json",
					"Authorization" -> ("Bearer " + getAuthTokenForEM))
				.get
		} catch {
			case ex : Exception => toJson(Map("error" -> "offline error"))
		}
	}
}