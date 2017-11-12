package com.pharbers.message.websocket

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import scala.concurrent.stm._

trait Socket {
	def connect(implicit system: ActorSystem, mate: Materializer): play.api.mvc.WebSocket = {
		play.api.mvc.WebSocket.accept[JsValue, JsValue] { request =>
			ActorFlow.actorRef ( out => WebSocketActor.props(out) )
		}
	}
	def sendMsg(msg: JsValue, uid: String): Unit
	def openHeartbeat()
}

case class WebSocket() extends Socket {
	import WebSocketOutActorRef._
	override def sendMsg(msg: JsValue, uid: String): Unit =
		outActorRefSeq.single.get.filterNot(f => f._1 != uid) foreach (x => x._2 ! msg)

	override def openHeartbeat(): Unit = ???
}
