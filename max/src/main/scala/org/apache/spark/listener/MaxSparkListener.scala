package org.apache.spark.listener

import org.apache.spark.scheduler._
import akka.actor.{Actor, ActorRef}
import com.pharbers.channel.util.sendEmTrait
import org.apache.spark.listener.listenerActor._

/**
  * Created by spark on 18-5-2.
  *
  * Spark 的 SparkListener 提供针对 application, job, task的状态监听。
  * 通过该监听，可以实现：
  * 1.任务执行进度的粗略计算。
  * 2.执行异常失败时，获取异常信息。
  * 3.获取app启动的appId,从而可以控制杀死任务。
  * 4.自定义进度和异常的handle处理（如控制台打印，保存db，或jms传输到web等终端
  *
  * @param start_progress   开始进度。
  * @param end_progress     任务完成时进度。
  */
case class MaxSparkListener(start_progress: Int, end_progress: Int, tag: String = "")
                           (implicit send: ((sendEmTrait, Double, String) => Unit), acc: Actor) extends SparkListener {

    require(0 <= start_progress && start_progress <= end_progress, "progress factor is error")
    require(end_progress <= 100, "progress factor is error")

    val lactor: ActorRef = acc.context.actorOf(listenerActor.props(start_progress, end_progress, tag))

    override def onJobStart(job: SparkListenerJobStart): Unit =
        lactor ! jobStart(job.stageInfos.map(stageInfo => stageInfo.numTasks).sum)

    override def onTaskEnd(task: SparkListenerTaskEnd): Unit =
        lactor ! taskEnd()

    override def onJobEnd(job: SparkListenerJobEnd): Unit =
        lactor ! jobEnd(this)
}


