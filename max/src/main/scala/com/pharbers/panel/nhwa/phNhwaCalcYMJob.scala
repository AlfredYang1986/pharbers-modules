package com.pharbers.panel.nhwa

import java.util.UUID

import akka.actor.Actor
import com.pharbers.channel.sendEmTrait
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.jobs.sequenceJob
import com.pharbers.panel.common.phCalcYM2JVJob
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat
import org.apache.spark.listener
import org.apache.spark.listener.addListenerAction
import org.apache.spark.listener.progress.sendSingleProgress

object phNhwaCalcYMJob {
    def apply(_company: String, _user: String)
             (_cpa_file: String)(implicit _actor: Actor): phNhwaCalcYMJob = {
        new phNhwaCalcYMJob {
            override lazy val cpa_file: String = _cpa_file

            override lazy val user: String = _user
            override lazy val company: String = _company
            override lazy val actor: Actor = _actor
        }
    }
}

trait phNhwaCalcYMJob extends sequenceJob {
    override val name: String = "phNhwaCalcYMJob"
    val cache_location: String = max_path_obj.p_cachePath + UUID.randomUUID().toString
    val cpa_file: String
    val user,company: String

    implicit val actor: Actor
    implicit val sp: (sendEmTrait, Double) => Unit = sendSingleProgress(company, user).singleProgress

    override val actions: List[pActionTrait] = { jarPreloadAction() ::
                setLogLevelAction("ERROR") ::
                addListenerAction(listener.MaxSparkListener(0, 90)) ::
                xlsxReadingAction[phNhwaCpaFormat](cpa_file, "cpa") ::
                phNhwaCalcYMConcretJob() ::
                saveCurrenResultAction(cache_location) ::
                phCalcYM2JVJob() ::
                Nil
    }
}