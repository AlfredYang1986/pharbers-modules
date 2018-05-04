package com.pharbers.main

import akka.actor.ActorSystem
import com.pharbers.channel.callJobConsumer
import com.typesafe.config.ConfigFactory

/**
  * Created by spark on 18-4-24.
  */
object main extends App {
    val config = ConfigFactory.load("split-new-master")
    val system = ActorSystem("maxActor", config)

    callJobConsumer("max_calc")(system)
}