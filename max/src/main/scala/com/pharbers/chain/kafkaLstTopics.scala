package com.pharbers.chain

import java.util.Properties

import scala.collection.JavaConverters._
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.PartitionInfo

trait kafkaLstTopics {

    def lstTopics : List[String] = {
        val props = new Properties()

        props.put("bootstrap.servers", "localhost:9092")
//        props.put("group.id", "test-consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

        val consumer = new KafkaConsumer[String, String](props)
        consumer.listTopics().asScala.toList.map (x => x._1)
    }
}
