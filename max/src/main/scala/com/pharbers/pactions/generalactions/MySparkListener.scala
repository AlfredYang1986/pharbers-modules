package com.pharbers.pactions.generalactions

import org.apache.spark.scheduler._

/**
  * Created by spark on 18-5-2.
  */

class MySparkListener extends SparkListener {
    override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd) {
        println("*************************************************")
        println("app:end")
        println("*************************************************")
    }

    override def onJobEnd(jobEnd: SparkListenerJobEnd) {
        println("*************************************************")
        println("job:end")
        jobEnd.jobResult match {
            case JobSucceeded =>
                println("job:end:JobSucceeded")
            case _ => ???
        }
        println("*************************************************")
    }
}