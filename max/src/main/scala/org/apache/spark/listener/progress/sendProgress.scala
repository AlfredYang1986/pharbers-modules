package org.apache.spark.listener.progress

import play.api.libs.json.Json.toJson
import com.pharbers.channel.sendEmTrait
import com.pharbers.common.algorithm.alTempLog

sealed trait sendProgress{
    val company: String
    val user: String
}

case class sendSingleProgress(company: String, user: String) extends sendProgress {

    implicit val singleProgress: (sendEmTrait, Double) => Unit = { (em, progress) =>
        em.sendMessage(company, user, "ymCalc", "ing", toJson(Map("progress" -> toJson(progress))))
        alTempLog(s"$company $user current progress = " + progress.toInt)
    }
}


case class sendMultiProgress(company: String, user: String, stage: String)
                            (p_current: Double, p_total: Double) {

    var previousProgress = 0
    implicit val multiProgress: (sendEmTrait, Double) => Unit = { (em, progress) =>
        val currentprogress = ((p_current - 1) / p_total * 100 + progress / p_total).toInt
        if(currentprogress > previousProgress){
            em.sendMessage(company, user, stage, "ing", toJson(Map("progress" -> toJson(currentprogress))))
            alTempLog(s"$company $user current progress = " + currentprogress)
            previousProgress = currentprogress
        }
    }
}