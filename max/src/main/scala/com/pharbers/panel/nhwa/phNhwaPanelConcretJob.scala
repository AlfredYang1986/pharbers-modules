package com.pharbers.panel.nhwa

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver

// TODO : job 和 Impl是可以合并的
object phNhwaPanelConcretJob {
    def apply(company: String, cache_dir: String, ym: List[String], mkt: String): pActionTrait =
        new phNhwaPanelConcretJob(company, cache_dir, ym, mkt)
}

class phNhwaPanelConcretJob(company: String, cache_dir: String,
                            ym: List[String], mkt: String) extends pActionTrait {
    override val defaultArgs = NULLArgs

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        val panelHander = phNhwaPanelConcretJobImpl(company, cache_dir)(phSparkDriver())
        panelHander.getPanelFile(ym, mkt)
        NULLArgs
    }
}