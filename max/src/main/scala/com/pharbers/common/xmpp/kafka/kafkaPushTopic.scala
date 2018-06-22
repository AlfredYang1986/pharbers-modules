package com.pharbers.common.xmpp.kafka

import java.util.Properties

import com.pharbers.common.algorithm.alTempLog
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}

import scala.collection.JavaConverters._

trait kafkaPushTopic { this : kafkaBasicConf =>

    val partitions : Int = 1
    val replication : Int = 1

    def pushTopic(name : String) : Unit = {
        val config = new Properties
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, endpoints)
        val admin = AdminClient.create(config)

        admin.createTopics(
            (new NewTopic(name,
                partitions.asInstanceOf[Number].intValue,
                replication.asInstanceOf[Number].shortValue
            ).configs(Map.empty[String, String].asJava) :: Nil)
                    .asJava
        ).values.asScala.map { x =>
            try {
                x._2.get
                alTempLog(s"topic ${x._1} created")
            } catch {
                case _ : Exception => alTempLog(s"topic ${x._1} existed")
            }
        }
    }
}
