package com.pharbers.max

import com.pharbers.calc.phMaxScheduleJob
import com.pharbers.timer.TimerJob

/**
  * Created by jeorch on 18-5-23.
  */
object MaxSyncDataTest extends App {
    TimerJob(new phMaxScheduleJob().getClass.getName).start(0, 10 * 60)
}
