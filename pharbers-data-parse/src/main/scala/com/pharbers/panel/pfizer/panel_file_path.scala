package com.pharbers.panel.pfizer

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-9-21.
  */
trait panel_file_path extends PharbersInjectModule {
    override val id: String = "data-parse"
    override val configPath: String = "pharbers_config/data_parse_file.xml"
    override val md = "contrast_file_path" :: "product_vs_ims_file" ::
                    "universe_inf_file" :: "markets_file" :: "cache_file" ::
                    "output_file" :: "makets" :: Nil

    val base_path: String = config.mc.find(p => p._1 == "contrast_file_path").get._2.toString
    val product_vs_ims_file: String = config.mc.find(p => p._1 == "product_vs_ims_file").get._2.toString
    val universe_inf_file: String = config.mc.find(p => p._1 == "universe_inf_file").get._2.toString
    val markets_file: String = config.mc.find(p => p._1 == "markets_file").get._2.toString
    val cache_local: String = config.mc.find(p => p._1 == "cache_file").get._2.toString
    val output_local: String = config.mc.find(p => p._1 == "output_file").get._2.toString
    val makets: String = config.mc.find(p => p._1 == "makets").get._2.toString
}
