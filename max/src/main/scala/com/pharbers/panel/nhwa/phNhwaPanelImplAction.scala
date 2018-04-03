package com.pharbers.panel.nhwa

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver

object phNhwaPanelImplAction {
    def apply(company: String, cache_dir: String, ym: List[String], mkt: String): pActionTrait =
        new phNhwaPanelImplAction(company, cache_dir, ym, mkt)
}

class phNhwaPanelImplAction(company: String, cache_dir: String,
                            ym: List[String], mkt: String) extends pActionTrait {
    override val defaultArgs = NULLArgs

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        val panelHander = new phNhwaPanelImpl(company, cache_dir)(phSparkDriver())
        panelHander.getPanelFile(ym, mkt)
        NULLArgs
    }
}