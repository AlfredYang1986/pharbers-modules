package org.apache.spark.listener

import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.channel.util.sendEmTrait
import com.pharbers.common.algorithm.alTempLog
import org.apache.spark.listener.listenerActor._
import org.apache.spark.scheduler.SparkListener
import com.pharbers.pactions.actionbase.NULLArgs

object listenerActor {
    def name = "listenerActor"
    def props(start_progress: Int, end_progress: Int, tag: String)
             (implicit send: ((sendEmTrait, Double, String) => Unit)) =
        Props(new listenerActor(start_progress, end_progress, tag))

    case class jobStart(taskSum: Int)
    case class taskEnd()
    case class jobEnd(listener: SparkListener)
}


class listenerActor(start_progress: Int, end_progress: Int, tag: String)
                   (implicit send: ((sendEmTrait, Double, String) => Unit)) extends Actor with ActorLogging with sendEmTrait {

    // 当前进度
    private var current: Double = start_progress
    // 剩余任务数
    private var remainTask: Int = 0
    // 步长
    private var stride: Double = 0.0
    // 返回的取整后的进度
    private var progress: Int = 0

    override def receive = {
        case jobStart(taskSum) => {
            remainTask += taskSum
            stride = (end_progress - current) / remainTask
//            alTempLog("Job Start, new tesk sum = " + taskSum)
//            alTempLog("remain task sum = " + remainTask)
        }

        case taskEnd() => {
            remainTask -= 1
            current += stride

            if(progress < current.toInt){
                progress = current.toInt
                send(this, progress, tag)
            }
        }

        case jobEnd(listener) =>
            if(remainTask < 1)
                alTempLog("remainTask = " + remainTask)
                removeListenerAction(listener).perform(NULLArgs)
//            alTempLog("Job Over")

        case _ => ???
    }
}
