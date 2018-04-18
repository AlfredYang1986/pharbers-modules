package com.pharbers.chain

import java.io.File
import java.util
import java.util.Properties

import kafka.consumer.KafkaStream
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.{DatumReader, Decoder, DecoderFactory}
import org.apache.avro.specific.SpecificDatumReader
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
//import org.apache.kafka.streams.KafkaStreams
import scala.collection.JavaConverters._

trait kafkaConsumer extends Runnable {

    val topic : String
    val group_id : String

    lazy val consumer = {
        val props = new Properties()
        props.put("bootstrap.servers", "localhost:9092")
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
                val schema = new Schema.Parser().parse(new File("pharbers_config/progress.arsc"))
                val reader = new SpecificDatumReader[GenericRecord](schema)
                val decoder = DecoderFactory.get().binaryDecoder(received_message, null)
                val payload = reader.read(null, decoder)
                println("Message received : " + payload)
            }
        }
    }
}
