package com.pharbers

import akka.actor.ActorSystem
import com.pharbers.channel.chanelImpl.callJobConsumer

/**
  * Created by spark on 18-4-24.
  */
object main extends App {
    val system = ActorSystem("maxActor")

    callJobConsumer("max_calc")(system)
    println("MAX Driver started:wq")
}