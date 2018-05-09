package com.pharbers.common.xmpp.kafka

import java.io.File
import akka.pattern.ask
import akka.util.Timeout
import java.util.Properties
import org.apache.avro.Schema
import scala.concurrent.Await
import scala.language.postfixOps
import scala.concurrent.duration._
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import akka.actor.{ActorSystem, Props}
import scala.collection.JavaConverters._
import org.apache.avro.io.DecoderFactory
import com.pharbers.bmpattern.RoutesActor
import org.apache.avro.generic.GenericRecord
import com.pharbers.common.algorithm.alTempLog
import org.apache.avro.specific.SpecificDatumReader
import com.pharbers.bmmessages.{MessageRoutes, excute}
import org.apache.kafka.clients.consumer.KafkaConsumer

trait kafkaConsumer extends Runnable { this : kafkaBasicConf =>
    val group_id : String

    implicit val t: Timeout = 2 seconds
    implicit val dispatch: ActorSystem
    val consumeHandler: JsValue => MessageRoutes

    def commonExcution(msr: MessageRoutes): Unit = {
        val act = dispatch.actorOf(Props[RoutesActor])
        val r = act ? excute(msr)
        val result = Await.result(r.mapTo[JsValue], t.duration)
    }

    implicit val content : GenericRecord => JsValue = { record =>
/// 杨总本意是接受的消息如果result不为空，则只保留result
//        val tmp = record.get("result")
//        if (record.get("result") != null) toJson(tmp.toString)
//        else {
            toJson(
                record.getSchema.getFields.asScala.map { x =>
                    record.get(x.name) match {
                        case null => None
                        case value => Some(x.name -> value.toString)
                    }
                }.filter(_.isDefined).map (_.get).toMap
            )
//        }
    }

    lazy val consumer: KafkaConsumer[String, Array[Byte]] = {
        val props = new Properties()
        props.put("bootstrap.servers", endpoints)
        props.put("group.id", group_id)
        props.put("enable.auto.commit", "true")
        props.put("auto.commit.interval.ms", "1000")
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")

        val tmp = new KafkaConsumer[String, Array[Byte]](props)
        tmp.subscribe((topic :: Nil).asJava)
        tmp
    }

    override def run(): Unit = {
        while (true) {
            consumer.poll(100).asScala.toList.foreach { record =>
                val schema = new Schema.Parser().parse(new File(schemapath))
                val reader = new SpecificDatumReader[GenericRecord](schema)
                val decoder = DecoderFactory.get().binaryDecoder(record.value, null)
                val payload = reader.read(null, decoder)
                alTempLog(s"Received message : " + payload)

                commonExcution(consumeHandler(content(payload)))
            }
        }
    }
}
