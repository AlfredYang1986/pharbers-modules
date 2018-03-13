package com.pharbers.panel

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-9-21.
  */
trait phPanelFilePath extends PharbersInjectModule {
    override val id: String = "data-parse"
    override val configPath: String = "pharbers_config/panel_config.xml"
    override val md = "base_path" :: "client_file_path" ::
                    "product_match_file" :: "markets_match_file" :: "universe_file" ::
                    "not_arrival_hosp_file" :: "not_published_hosp_file" :: "fill_hos_data_file" ::
                    "output_path" ::  Nil

    val base_path: String = config.mc.find(p => p._1 == "base_path").get._2.toString
    val client_path: String = config.mc.find(p => p._1 == "client_file_path").get._2.toString

    val product_match_file: String = config.mc.find(p => p._1 == "product_match_file").get._2.toString
    val markets_match_file: String = config.mc.find(p => p._1 == "markets_match_file").get._2.toString
    val universe_file: String = config.mc.find(p => p._1 == "universe_file").get._2.toString
    val not_arrival_hosp_file: String = config.mc.find(p => p._1 == "not_arrival_hosp_file").get._2.toString
    val not_published_hosp_file: String = config.mc.find(p => p._1 == "not_published_hosp_file").get._2.toString
    val fill_hos_data_file: String = config.mc.find(p => p._1 == "fill_hos_data_file").get._2.toString

    val output_path: String = config.mc.find(p => p._1 == "output_path").get._2.toString
}
