package com.pharbers.chain

import java.io.File
import java.util.Properties

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.io.EncoderFactory
import org.apache.avro.specific.SpecificDatumWriter
import org.apache.commons.io.output.ByteArrayOutputStream
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}

trait kafkaPushRecord {
    def pushRecord(id : String, name : String, stages : String, progress : Int)(topic : String) = {
        val props = new Properties()

        props.put("bootstrap.servers", "localhost:9092")
        props.put("acks", "all")
        props.put("retries", Integer.valueOf(0))
        props.put("batch.size", Integer.valueOf(16384))
        props.put("linger.ms", Integer.valueOf(1))
        props.put("buffer.memory", Integer.valueOf(33554432))
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
        props.put("serializer.class", "kafka.serializer.DefaultEncoder")

        val producer : Producer[String, Array[Byte]] = new KafkaProducer(props)

        val schema = new Schema.Parser().parse(new File("pharbers_config/progress.arsc"))
        val payload = new GenericData.Record(schema)
        payload.put("id", id)
        payload.put("name", name)
        payload.put("stages", stages)
        payload.put("progress", progress)
        println(s"Original Message : " + payload)

        val writer = new SpecificDatumWriter[GenericRecord](schema)
        val out = new ByteArrayOutputStream()
        val encoder = EncoderFactory.get().binaryEncoder(out, null)
        writer.write(payload, encoder)
        encoder.flush()
        out.close()

        val serializedBytes  = out.toByteArray
        println(s"Sending message in bytes : " + serializedBytes)

        val message = new ProducerRecord[String, Array[Byte]](topic, serializedBytes)
        producer.send(message)
        producer.close()
    }
}
