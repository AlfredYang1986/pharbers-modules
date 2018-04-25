package com.pharbers.main

import akka.actor.ActorSystem
import java.util.concurrent.Executors
import com.pharbers.channel.msgChannel
import com.typesafe.config.ConfigFactory

/**
  * Created by spark on 18-4-24.
  */
object main extends App{
    val config = ConfigFactory.load("split-new-master")
    val system = ActorSystem("max", config)

    val channel = msgChannel("abc")(system)

    Executors.newFixedThreadPool(1).submit(channel)
}