package com.pharbers.timer

import java.util.{Date, Timer, TimerTask}

import com.pharbers.pactions.actionbase.{MapArgs, pActionTrait}

case class TimerJob(jobClazz: String) {

    val task: TimerTask = new TimerTask() {
        override def run(): Unit = Class.forName(jobClazz).newInstance().asInstanceOf[pActionTrait].perform(MapArgs(Map.empty))
    }
    val timer: Timer = new Timer()

    def start(delayTime: Long): Unit = timer.schedule(task, delayTime * 1000)
    def start(startDate: Date): Unit = timer.schedule(task, startDate)
    def start(delayTime: Long, period: Long): Unit = timer.schedule(task, delayTime * 1000, period * 1000)
    def start(startDate: Date, period: Long): Unit = timer.schedule(task, startDate, period * 1000)

}
