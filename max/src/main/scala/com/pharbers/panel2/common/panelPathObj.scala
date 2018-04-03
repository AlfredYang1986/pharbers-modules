package com.pharbers.panel2.common

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-9-21.
  */
object panelPathObj extends PharbersInjectModule {
    override val id: String = "panel_config2"
    override val configPath: String = "pharbers_config/panel_config2.xml"
    override val md = "base_path" :: "match_file_dir" :: "result_dir" :: "cache_dir" :: Nil

    val p_base_path: String = config.mc.find(p => p._1 == "base_path").get._2.toString
    val p_match_file_dir: String = config.mc.find(p => p._1 == "match_file_dir").get._2.toString
    val p_result_dir: String = config.mc.find(p => p._1 == "result_dir").get._2.toString
    val p_cache_dir: String = config.mc.find(p => p._1 == "cache_dir").get._2.toString
}