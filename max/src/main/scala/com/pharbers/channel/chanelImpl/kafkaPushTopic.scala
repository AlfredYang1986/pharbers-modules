package com.pharbers.channel.chanelImpl

import java.util.Properties
import scala.collection.JavaConverters._
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, NewTopic}

trait kafkaPushTopic { this : kafkaBasicConf =>

    val partitions : Int = 1
    val replication : Int = 1

    def pushTopic(name : String) : Unit = {
        val config = new Properties
//        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
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
                println(s"topic ${x._1} created")
            } catch {
                case _ : Exception => println(s"topic ${x._1} existed")
            }
        }
    }
}
