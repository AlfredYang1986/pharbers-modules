package com.pharbers.channel.syncchannel

import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.common.xmpp.kafka._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import akka.pattern.ask
import akka.util.Timeout
import com.pharbers.bmmessages.{CommonModules, MessageRoutes}
import com.pharbers.bmpattern.LogMessage.msg_log
import com.pharbers.bmpattern.ResultMessage.msg_CommonResultMessage
import com.pharbers.channel.syncchannel.syncHelper.syncMessage.msg_syncCallback
import com.pharbers.channel.syncchannel.syncHelper.syncModule

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * kafka 怎么是xmpp下的子包？？？？？？
  */
trait psync extends kafkaBasicConf with kafkaConsumer with kafkaPushRecord {

    implicit val time_out : Timeout = 5 second
    lazy val actor = dispatch.actorOf(syncActor.props(this))

    object syncActor {
        def props(t : psync) = Props(new syncActor(t))
    }

    class syncActor(t : psync) extends Actor with ActorLogging {
        override def receive: Receive = {
            case mmp : Map[String, AnyRef] => t.pushRecord(mmp)(precord)
            case _ => ???
        }
    }

    def syncCall(m : Map[String, AnyRef]) : JsValue = {
        val actor = dispatch.actorOf(syncActor.props(this))
        val r = actor ? m
        val result = Await.result(r.mapTo[JsValue], time_out.duration)
        result
    }

    override val consumeHandler: JsValue => MessageRoutes = { jv =>
        import com.pharbers.bmpattern.LogMessage.common_log
        import com.pharbers.bmpattern.ResultMessage.common_result
        MessageRoutes(msg_log(toJson(Map("method" -> toJson("call job request"))), jv)
            :: msg_syncCallback(jv)(new syncModule(actor))
            :: msg_CommonResultMessage() :: Nil, None)(CommonModules(Some(Map("as" -> dispatch))))
    }
}
