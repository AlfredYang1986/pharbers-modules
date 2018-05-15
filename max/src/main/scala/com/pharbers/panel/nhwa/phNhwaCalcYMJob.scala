package com.pharbers.panel.nhwa

import java.util.UUID
import akka.actor.Actor
import com.pharbers.channel.sendEmTrait
import com.pharbers.pactions.generalactions._
import com.pharbers.pactions.jobs.sequenceJob
import com.pharbers.panel.common.phCalcYM2JVJob
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.panel.nhwa.format.phNhwaCpaFormat
import org.apache.spark.listener.progress.sendSingleProgress
import org.apache.spark.listener.{MaxSparkListener, addListenerAction}

case class phNhwaCalcYMJob(args: Map[String, String])(implicit _actor: Actor) extends sequenceJob {
    override val name: String = "phNhwaCalcYMJob"
    val cache_location: String = max_path_obj.p_cachePath + UUID.randomUUID().toString
    val cpa_file: String = max_path_obj.p_clientPath + args("cpa")
    val user_id: String = args("user_id")
    val company_id: String = args("company_id")

    implicit val sp: (sendEmTrait, Double) => Unit = sendSingleProgress(company_id, user_id).singleProgress

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