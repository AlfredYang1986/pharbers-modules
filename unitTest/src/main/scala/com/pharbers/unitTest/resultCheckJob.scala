package com.pharbers.unitTest

import java.util.UUID
import akka.actor.Actor
import com.pharbers.unitTest.action._
import com.pharbers.pactions.generalactions._
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.excel.input.PhExcelXLSXCommonFormat
import com.pharbers.pactions.jobs.{sequenceJob, sequenceJobWithMap}
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs, pActionTrait}

case class resultCheckJob(args: Map[String, String])
                         (implicit _actor: Actor) extends sequenceJobWithMap {
    override val name: String = "result_check_job"
    
    val df = MapArgs(args.map(x => x._1 -> StringArgs(x._2)))
    
    // 加载线下结果文件
    val loadOfflineResult: sequenceJob = new sequenceJob {
        val temp_name: String = UUID.randomUUID().toString
        val temp_dir: String = max_path_obj.p_cachePath
        override val  name = "offline_result"
        override val actions: List[pActionTrait] =
            xlsxReadingAction[PhExcelXLSXCommonFormat](args("offlineResult"), temp_name) ::
                    saveCurrenResultAction(temp_dir + temp_name) ::
                    csv2DFAction(temp_dir + temp_name) :: Nil
    }
    
    override val actions: List[pActionTrait] = { jarPreloadAction() ::
                setLogLevelAction("ERROR") ::
                executeMaxAction(df) ::
                loadUnitTestJarAction() ::
                loadOfflineResult ::
                resultCheckAction(df) ::
                writeCheckResultAction(df) ::
                Nil
    }
}
