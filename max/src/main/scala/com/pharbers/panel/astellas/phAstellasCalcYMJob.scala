package com.pharbers.panel.astellas

import java.util.UUID

import akka.actor.Actor
import com.pharbers.channel.util.sendEmTrait
import com.pharbers.panel.common._
import com.pharbers.panel.astellas.format._
import com.pharbers.pactions.generalactions._
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.jobs.sequenceJobWithMap
import org.apache.spark.listener.progress.sendSingleProgress
import org.apache.spark.listener.{MaxSparkListener, addListenerAction}

case class phAstellasCalcYMJob(args: Map[String, String])(implicit _actor: Actor) extends sequenceJobWithMap {
    override val name: String = "phAstellasCalcYMJob"
    lazy val cpa_file: String = max_path_obj.p_clientPath + args("cpa")
    lazy val gyc_file: String = max_path_obj.p_clientPath + args("gycx")
    lazy val cache_location: String = max_path_obj.p_cachePath + UUID.randomUUID().toString

    lazy val user_id: String = args("user_id")
    lazy val company_id: String = args("company_id")
    implicit val sp: (sendEmTrait, Double, String) => Unit = sendSingleProgress(company_id, user_id).singleProgress

    override val actions: List[pActionTrait] = jarPreloadAction() ::
            setLogLevelAction("ERROR") ::
            xlsxReadingAction[phAstellasCpaFormat](cpa_file, "cpa") ::
            xlsxReadingAction[phAstellasGycxFormat](gyc_file, "gycx") ::
            addListenerAction(MaxSparkListener(0, 50, "phAstellasCalcYMCpaConcretJob")) ::
            phAstellasCalcYMCpaConcretJob() ::
            phAstellasCalcYMGycxConcretJob() ::
            saveMapResultAction("calcYMWithCpa", cache_location + "cpa") ::
            saveMapResultAction("calcYMWithGycx", cache_location + "gycx") ::
            addListenerAction(MaxSparkListener(51, 99, "phCalcYM2JVJobWithCpaAndGyc")) ::
            phCalcYM2JVJobWithCpaAndGyc() ::
            Nil
} 