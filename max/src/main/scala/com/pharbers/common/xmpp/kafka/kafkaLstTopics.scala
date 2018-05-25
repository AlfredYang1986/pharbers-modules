package com.pharbers.common.xmpp.kafka

import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._

trait kafkaLstTopics { this : kafkaBasicConf =>

    def lstTopics: List[String] = {
        val props = new Properties()

        props.put("bootstrap.servers", endpoints)
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

        val consumer = new KafkaConsumer[String, String](props)
        consumer.listTopics().asScala.toList.map (x => x._1)
    }
}
