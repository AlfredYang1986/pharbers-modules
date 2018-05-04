package com.pharbers.message.websocket

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.pharbers.message.common.MessageTrait
import com.pharbers.moduleConfig.{ConfigDefines, ConfigImpl}
import play.api.libs.json.JsValue
import play.api.libs.streams._
import play.api.mvc._
import play.api.mvc.Results.Forbidden

import scala.concurrent.Future
import scala.xml.Node

trait Socket extends SameOriginCheck {

	def connect(implicit system: ActorSystem, mate: Materializer): play.api.mvc.WebSocket = play.api.mvc.WebSocket.acceptOrResult[JsValue, JsValue] { request =>
		Future.successful(
			if (sameOriginCheck(request))
				Right(ActorFlow.actorRef ( out => WebSocketActor.props(out) ))
			else Left(Forbidden))
	}
	
//	def connect(implicit system: ActorSystem, mate: Materializer): play.api.mvc.WebSocket = {
//		play.api.mvc.WebSocket.accept[JsValue, JsValue] { request =>
//			ActorFlow.actorRef ( out => WebSocketActor.props(out) )
//		}
//	}
	
	def sendMsg(msg: JsValue, uid: String): Unit
	def openHeartbeat()
}

case class WebSocket() extends Socket {
	import WebSocketOutActorRef._
	
	
	override def sendMsg(msg: JsValue, uid: String): Unit =
		outActorRefSeq.single.get.filterNot(f => f._1 != uid) foreach (x => x._2 ! msg)

	override def openHeartbeat(): Unit = ???
}

trait SameOriginCheck extends MessageTrait{
	override val id: String = "message-content-nodes"
	override val configPath: String = "pharbers_config/message_manager.xml"
	override val md: List[String] = "content-config-path" :: Nil
	
	import com.pharbers.moduleConfig.ModuleConfig.fr
	override lazy val config: ConfigImpl = loadConfig(configDir + "/" + configPath)
	
	implicit val format : (ConfigDefines, Node) => ConfigImpl = { (c, n) =>
		ConfigImpl(
			c.md map { x => x -> ((n \ x).toList map { iter =>
				(iter \\ "@name").toString -> new WebSocketInstance((iter \\ "@value").toString)
			})}
		)
	}
	
	val wSocket: WebSocketContentInfo = queryMessageInstance("wsocket").asInstanceOf[WebSocketContentInfo]
	def sameOriginCheck(rh: RequestHeader): Boolean = {
		rh.headers.get("Origin") match {
			case Some(originValue) if originMatches(originValue) => true
			
			case Some(badOrigin) => false
			
			case None => false
		}
	}
	
	def originMatches(origin: String): Boolean = {
		origin.contains(wSocket.remoteConnect) || origin.contains(wSocket.localConnect)
	}
}