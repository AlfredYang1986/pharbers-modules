package com.pharbers.delivery.astellas

import com.pharbers.paction.actionbase.{NULLArgs, RDDArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.driver.phSparkDriver

/**
  * Created by jeorch on 18-4-2.
  */
object phReadAstellasHistoryDataAction {
    def apply(historyFilePath: String): pActionTrait = new phReadAstellasHistoryDataAction(historyFilePath)
    def apply(historyFilePath: String, nickname: String): pActionTrait = {
        val temp = new phReadAstellasHistoryDataAction(historyFilePath)
        temp.name = nickname
        temp
    }
}

class phReadAstellasHistoryDataAction(historyFilePath: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {
        val sc = phSparkDriver().sc
        RDDArgs(sc.textFile(historyFilePath))
    }

}
