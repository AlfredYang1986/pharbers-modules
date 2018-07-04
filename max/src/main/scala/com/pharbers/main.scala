package com.pharbers

import akka.actor.ActorSystem
import com.pharbers.calc.phMaxScheduleJob
import com.pharbers.channel.chanelImpl.callJobConsumer
import com.pharbers.common.algorithm.alTempLog
import com.pharbers.timer.TimerJob

/**
  * Created by spark on 18-4-24.
  */
object main extends App {
    val system = ActorSystem("maxActor")

    TimerJob(new phMaxScheduleJob().getClass.getName).start(0, 24 * 60 * 60)
    callJobConsumer("max_calc")(system)
    alTempLog("MAX Driver started")
}