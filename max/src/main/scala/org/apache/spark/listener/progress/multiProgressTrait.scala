package org.apache.spark.listener.progress

import com.pharbers.channel.sendEmTrait
import com.pharbers.common.algorithm.alTempLog
import play.api.libs.json.Json.toJson

trait multiProgressTrait extends singleProgressTrait {

    val p_total: Double
    val p_current: Double

    implicit val multiProgress: (sendEmTrait, Double) => Unit = { (em, progress) =>
        val p = (p_current - 1) / p_total * 100 + progress / p_total
        em.sendMessage(company, user, "panel", "ing", toJson(Map("progress" -> toJson(p.toInt))))
        alTempLog(s"$company $user current progress = " + p.toInt)
    }
}
