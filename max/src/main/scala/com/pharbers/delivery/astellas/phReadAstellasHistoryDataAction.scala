package com.pharbers.delivery.astellas

import com.pharbers.pactions.actionbase.{NULLArgs, RDDArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.phSparkDriver

/**
  * Created by jeorch on 18-4-2.
  */
object phReadAstellasHistoryDataAction {
    def apply(historyFilePath: String, name: String): pActionTrait = new phReadAstellasHistoryDataAction(historyFilePath, name)
}

class phReadAstellasHistoryDataAction(historyFilePath: String, override val name: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {
        val sc = phSparkDriver().sc
        RDDArgs(sc.textFile(historyFilePath))
    }

}
