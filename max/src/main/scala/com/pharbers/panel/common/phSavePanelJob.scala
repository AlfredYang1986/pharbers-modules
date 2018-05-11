package com.pharbers.panel.common

import com.pharbers.pactions.actionbase._
import com.pharbers.common.algorithm.max_path_obj

object phSavePanelJob  {
    def apply(args: MapArgs): pActionTrait = new phSavePanelJob(args)
}

class phSavePanelJob(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "phSavePanelJob"
    override def perform(pr: pActionArgs): pActionArgs = {
        val panel = pr.asInstanceOf[MapArgs].get("panel").asInstanceOf[DFArgs].get
        val panel_name = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val panel_location = max_path_obj.p_panelPath + panel_name

        panel.write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                .save(panel_location)

        StringArgs(panel_name)
    }
}