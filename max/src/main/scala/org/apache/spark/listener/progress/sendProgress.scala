package org.apache.spark.listener.progress

import com.pharbers.channel.util.sendEmTrait
import play.api.libs.json.Json.toJson
import com.pharbers.common.algorithm.alTempLog

sealed trait sendProgress{
    val company: String
    val user: String
}

case class sendSingleProgress(company: String, user: String) extends sendProgress {

    implicit val singleProgress: (sendEmTrait, Double, String) => Unit = { (em, progress, tag) =>
        em.sendMessage(company, user, "ymCalc", "ing", toJson(Map("progress" -> toJson(progress))))
        alTempLog(s"$company $user current $tag progress = " + progress.toInt)
    }
}


case class sendMultiProgress(company: String, user: String, stage: String)
                            (p_current: Double, p_total: Double) {

    var previousProgress = 0
    implicit val multiProgress: (sendEmTrait, Double, String) => Unit = { (em, progress, tag) =>
        val currentprogress = p_total match {
            case d: Double if d < 1 => 0
            case _ => ((p_current - 1) / p_total * 100 + progress / p_total).toInt
        }

        if(currentprogress > previousProgress){
            em.sendMessage(company, user, stage, "ing", toJson(Map("progress" -> toJson(currentprogress))))
            alTempLog(s"$company $user current $tag progress = " + currentprogress)
            previousProgress = currentprogress
        }
    }
}