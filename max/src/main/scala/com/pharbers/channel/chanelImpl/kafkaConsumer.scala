package com.pharbers.channel.chanelImpl

import java.io.File
import java.util.Properties

import akka.actor.{Actor, IndirectActorProducer, Props}
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout

import com.pharbers.bmmessages.{MessageRoutes, excute}
import com.pharbers.bmpattern.RoutesActor
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io.DecoderFactory
import org.apache.avro.specific.SpecificDatumReader
import org.apache.kafka.clients.consumer.KafkaConsumer
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.concurrent.Await
import scala.concurrent.duration._
//import org.apache.kafka.streams.KafkaStreams
import scala.collection.JavaConverters._

trait kafkaConsumer extends Runnable { this : kafkaBasicConf =>
    val group_id : String

    implicit val t : Timeout = 2 seconds
    implicit val dispatch : ActorSystem
    val consumeHandler : JsValue => MessageRoutes

    def commonExcution(msr : MessageRoutes) = {
        val act = dispatch.actorOf(Props[RoutesActor])
        val r = act ? excute(msr)
        val result = Await.result(r.mapTo[JsValue], t.duration)
        println(s"alfred test result is $result")
    }

    implicit val content : GenericRecord => JsValue = { record =>
        val tmp = record.get("result")
        if (record.get("result") != null) toJson(tmp.toString)
        else {
            toJson(
                record.getSchema.getFields.asScala.map { x =>
                    record.get(x.name) match {
                        case null => None
                        case value => Some(x.name -> value.toString)
                    }
                }.filter(_ != None).map (_.get).toMap
            )
        }
    }

    lazy val consumer = {
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
            consumer.poll(100).asScala.toList.map { record =>
                println(s"offset = ${record.offset}, key = ${record.key}, value = ${record.value}")

                val received_message = record.value
                println(s"received message is $received_message")
//                val schema = new Schema.Parser().parse(new File("pharbers_config/progress.arsc"))
                val schema = new Schema.Parser().parse(new File(schemapath))
                val reader = new SpecificDatumReader[GenericRecord](schema)
                val decoder = DecoderFactory.get().binaryDecoder(received_message, null)
                val payload = reader.read(null, decoder)
                println("Message received : " + payload)

                commonExcution(consumeHandler(content(payload)))
            }
        }
    }
}
