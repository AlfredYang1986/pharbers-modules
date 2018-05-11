package com.pharbers.pactions.generalactions

import com.pharbers.channel.sendEmTrait
import com.pharbers.pactions.actionbase.NULLArgs
import org.apache.spark.listener.removeListenerAction
import org.apache.spark.scheduler._
import play.api.libs.json.Json.toJson

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
case class MaxSparkListener(targetUser: String, stage: String)
                           (start_progress: Int, end_progress: Int) extends SparkListener {

    require(0 <= start_progress && start_progress <= end_progress, "progress factor is error")
    require(end_progress <= 100, "progress factor is error")

    // 当前进度
    private var current: Double = start_progress
    // 剩余任务数
    private var remainTask: Int = 0
    // 步长
    private var stride: Double = 0.0
    // 返回的取整后的进度
    private var progress: Int = 0

    private val em = new sendEmTrait{}

    override def onJobStart(jobStart: SparkListenerJobStart): Unit = synchronized{
        val tasks = jobStart.stageInfos.map(stageInfo => stageInfo.numTasks).sum
        remainTask += tasks
        stride = (end_progress - current) / remainTask
        println("onJobStart" + tasks)
        println("remainTask" + remainTask)
    }

    override def onTaskEnd(taskEnd: SparkListenerTaskEnd): Unit = synchronized{
        remainTask -= 1
        current += stride

        if(progress < current.toInt){
            progress = current.toInt
            em.sendMessage("", targetUser, stage, "ing", toJson(Map("progress" -> toJson(progress))))
            println("aaa" + progress)
        }
    }

    // job 结束，关闭监听
    override def onJobEnd(jobEnd: SparkListenerJobEnd): Unit = synchronized{
        if(remainTask == 0){
            implicit def progressFunc(progress: Double, flag : String) : Unit = {}
            removeListenerAction(this).perform(NULLArgs)
        }
    }

}


