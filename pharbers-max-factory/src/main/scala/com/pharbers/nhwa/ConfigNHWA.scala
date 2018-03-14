package com.pharbers.nhwa

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by jeorch on 18-3-14.
  */
trait ConfigNHWA extends PharbersInjectModule {
    override val id: String = "nhwa-config"
    override val configPath: String = "pharbers_config/nhwa-config.xml"
    override val md =
        "max_result_file" :: "hospital_match_file" :: "nhwa_match_file" ::
        "acc_match_file" :: "area_match_file" :: "market_match_file" ::
        "output_path" :: Nil

    protected val maxResultFile: String = config.mc.find(p => p._1 == "max_result_file").get._2.toString
    protected val hospitalMatchFile: String = config.mc.find(p => p._1 == "hospital_match_file").get._2.toString
    protected val nhwaMatchFile: String = config.mc.find(p => p._1 == "nhwa_match_file").get._2.toString
    protected val accMatchFile: String = config.mc.find(p => p._1 == "acc_match_file").get._2.toString
    protected val areaMatchFile: String = config.mc.find(p => p._1 == "area_match_file").get._2.toString
    protected val marketMatchFile: String = config.mc.find(p => p._1 == "market_match_file").get._2.toString
    protected val outputPath: String = config.mc.find(p => p._1 == "output_path").get._2.toString
}
