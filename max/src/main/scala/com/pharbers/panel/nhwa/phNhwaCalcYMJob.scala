package com.pharbers.panel.nhwa

import java.util.UUID

import akka.actor.Actor
import play.api.libs.json.Json.toJson
import com.pharbers.channel.sendEmTrait
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.jobs.sequenceJob
import com.pharbers.panel.common.phCalcYM2JVJob
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat
import org.apache.spark.{MaxSparkListener, addListenerAction}

object phNhwaCalcYMJob {
    def apply(_cpa_file: String)(implicit _actor: Actor): phNhwaCalcYMJob = {
        new phNhwaCalcYMJob {
            override lazy val actor: Actor = _actor
            override lazy val cpa_file: String = _cpa_file
        }
    }
}

trait phNhwaCalcYMJob extends sequenceJob {
    override val name: String = "phNhwaCalcYMJob"
    val cpa_file: String
    val cache_location: String = max_path_obj.p_cachePath + UUID.randomUUID().toString

    implicit val actor: Actor
    implicit val sendProgress: (sendEmTrait, Double) => Unit = { (em, progress) =>
        em.sendMessage("testUser", "ymCalc", "ing", toJson(Map("progress" -> toJson(progress))))
    }

    override val actions: List[pActionTrait] = { jarPreloadAction() ::
                setLogLevelAction("ERROR") ::
                addListenerAction(MaxSparkListener(0, 90)) ::
                xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                phNhwaCalcYMConcretJob() ::
                saveCurrenResultAction(cache_location) ::
                phCalcYM2JVJob() ::
                Nil
    }
}