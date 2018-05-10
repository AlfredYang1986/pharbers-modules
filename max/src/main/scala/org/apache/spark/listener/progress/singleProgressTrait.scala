package org.apache.spark.listener.progress

import akka.actor.Actor
import com.pharbers.channel.sendEmTrait
import com.pharbers.common.algorithm.alTempLog
import play.api.libs.json.Json.toJson

trait singleProgressTrait {
    implicit val actor: Actor

    val user: String
    val company: String

    implicit val singleProgress: (sendEmTrait, Double) => Unit = { (em, progress) =>
        em.sendMessage(company, user, "ymCalc", "ing", toJson(Map("progress" -> toJson(progress))))
        alTempLog(s"$company $user current progress = " + progress.toInt)
    }
}
