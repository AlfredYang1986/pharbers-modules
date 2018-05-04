package com.pharbers.pactions.generalactions

import scala.collection.mutable
import org.apache.spark.scheduler._

/**
  * Created by spark on 18-5-2.
  *
  * Spark 的 DeveloperApi 提供针对app, job, task的执行监听。
  * 通过该监听，可以实现：
  * 1.任务执行进度的粗略计算。
  * 2.执行异常失败时，获取异常信息。
  * 3.获取app启动的appId,从而可以控制杀死任务。
  * 4.自定义进度和异常的handle处理（如控制台打印，保存db，或jms传输到web等终端
  *
  * @param jobNum Application中Job个数。可以通过代码的提交查看spark日志查看到。
  */
class MaxSparkListener() extends SparkListener {

    // Job和Job信息（包括总task数，当前完成task数，当前Job百分比）的映射
    private val jobToJobInfo = new mutable.HashMap[Int, (Int, Int, Int)]
    // stageId和Job的映射，用户获取task对应的job
    private val stageToJob = new mutable.HashMap[Int, Int]
    // 完成的job数量
    private var finishJobNum = 0
    private var hasException: Boolean = false

    //获取job的task数量，初始化job信息
    override def onJobStart(jobStart: SparkListenerJobStart): Unit = {
        val jobId = jobStart.jobId
        println("jobID = " + jobId)
        val tasks = jobStart.stageInfos.map(stageInfo => stageInfo.numTasks).sum
        println("tasks = " + tasks)
        jobToJobInfo += (jobId ->(tasks, 0, 0))
        println("jobToJobInfo = " + jobToJobInfo)
//        jobStart.stageIds.map(stageId => stageToJob(stageId) = jobId)


        println("Job start")
    }

    //task结束时，粗略估计当前app执行进度。
    //估算方法：当前完成task数量/总task数量。总完成task数量按（job总数*当前job的task数。）
    override def onTaskEnd(taskEnd: SparkListenerTaskEnd) = synchronized {
//        val stageId = taskEnd.stageId
//        val jobId = stageToJob.get(stageId).get
//        val (totalTaskNum: Int, finishTaskNum: Int, percent: Int) = jobToJobInfo.get(jobId).get
//        val currentFinishTaskNum = finishTaskNum + 1
//        val newPercent = currentFinishTaskNum * 100 / (totalTaskNum * jobNum)
//        jobToJobInfo(jobId) = (totalTaskNum, currentFinishTaskNum, newPercent)
//
//        if (newPercent > percent) {
//            //hanlde application progress
//            val totalPercent = jobToJobInfo.values.map(_._3).sum
//            if (totalPercent <= 100){
//                //        handleAppProgress(totalPercent)
//            }
//        }

        println("Task end")
    }

    //job 结束，获取job结束的状态，异常结束可以将异常的类型返回处理。
    // handle处理自定义，比如返回给web端，显示异常log。
    override def onJobEnd(jobEnd: SparkListenerJobEnd) = synchronized {
//        jobEnd.jobResult match {
//            case JobSucceeded => finishJobNum += 1
//            case JobFailed(exception) if !hasException =>
//                hasException = true
//
//            // handle application failure
//            //        handleAppFailure(exception)
//            case _ =>
//        }


        println("job end")
    }

    //app结束时，将程序执行进度标记为 100%。
    //缺陷：SparkListenerApplicationEnd没有提供app的Exception的获取。这样，当程序在driver端出错时，
    //获取不到出错的具体原因返回给前端，自定义提示。比如（driver对app中的sql解析异常，还没有开始job的运行）

    /*** driver 端异常可通过主程序代码里 try catch获取到 ***/

    override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd) = synchronized {
//        val totalJobNum = jobToJobInfo.keySet.size
//        val totalPercent = jobToJobInfo.values.map(_._3).sum
//        //handle precision lose
//        if (!hasException && totalPercent == 99) {
//            //      handleAppProgress(100)
//        }
//        val msg = "执行失败"
//        if(totalJobNum == 0){
//            handleAppFailure(new Exception(msg))
//        }

        println("application end")
    }
}


