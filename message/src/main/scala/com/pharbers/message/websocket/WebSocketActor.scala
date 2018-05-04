package com.pharbers.message.websocket

import akka.actor.{Actor, ActorRef, Props}
import play.api.libs.json.JsValue

import scala.concurrent.stm._

object WebSocketOutActorRef {
	val outActorRefSeq = Ref(Seq[(String, ActorRef)]())
}

object WebSocketActor {
	def props(out: ActorRef) = Props(new WebSocketActor(out))
}

class WebSocketActor(out: ActorRef) extends Actor{
	import WebSocketOutActorRef._
	def message: Receive = {
		case msg: JsValue =>
			atomic { implicit thx =>
				val uid = (msg \ "uid").asOpt[String].getOrElse("")
				outActorRefSeq() = outActorRefSeq.single.get :+ (uid, out)
			}
		case msg: String => out ! msg
		case _ => throw new Exception("actor is not impl")
	}
	
	override def receive: Receive = message
	
	override def postStop(): Unit = {
		atomic { implicit thx =>
			outActorRefSeq() = outActorRefSeq.single.get.filterNot(x => x._2 == out)
		}
		super.postStop()
	}
}
