package com.pharbers.common.xmpp.kafka

import java.io.File
import java.util.Properties

import com.pharbers.common.algorithm.alTempLog
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io.EncoderFactory
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

import scala.collection.JavaConverters._

trait kafkaPushRecord { this : kafkaBasicConf =>

    implicit val precord : (GenericData.Record, Map[String, Any]) => Unit = (record, m) =>
        record.getSchema.getFields.asScala.foreach(x =>
            m.get(x.name()).map(y => record.put(x.name(), y)).getOrElse(Unit))

    def pushRecord(m : Map[String, AnyRef])(implicit f : (GenericData.Record, Map[String, Any]) => Unit): Unit = {
        val props = new Properties()

        props.put("bootstrap.servers", endpoints)
        props.put("acks", "all")
        props.put("retries", Integer.valueOf(0))
        props.put("batch.size", Integer.valueOf(16384))
        props.put("linger.ms", Integer.valueOf(1))
        props.put("buffer.memory", Integer.valueOf(33554432))
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
        props.put("serializer.class", "kafka.serializer.DefaultEncoder")

        val producer : Producer[String, Array[Byte]] = new KafkaProducer(props)

        val schema = new Schema.Parser().parse(new File(schemapath))
        val payload = new GenericData.Record(schema)
        f(payload, m)
        alTempLog(s"Original Message : " + payload)

        val writer = new SpecificDatumWriter[GenericRecord](schema)
        val out = new ByteArrayOutputStream()
        val encoder = EncoderFactory.get().binaryEncoder(out, null)
        writer.write(payload, encoder)
        encoder.flush()
        out.close()

        val serializedBytes = out.toByteArray

        val message = new ProducerRecord[String, Array[Byte]](topic, serializedBytes)
        producer.send(message)
        producer.close()
    }
}
