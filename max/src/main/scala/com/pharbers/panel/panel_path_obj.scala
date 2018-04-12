package com.pharbers.panel

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-9-21.
  */
object panel_path_obj extends PharbersInjectModule {
    override val id: String = "panel_config"
    override val configPath: String = "pharbers_config/panel_config.xml"
    override val md = "match_file_dir" :: "result_dir" :: "cache_dir" :: Nil

    val p_matchFilePath: String = config.mc.find(p => p._1 == "match_file_dir").get._2.toString
    val p_resultPath: String = config.mc.find(p => p._1 == "result_dir").get._2.toString
    val p_cachePath: String = config.mc.find(p => p._1 == "cache_dir").get._2.toString

    val p_base_path: String = "p_base_path"
    val p_client_path: String = "p_client_path"
    val p_universe_file: String = "p_universe_file"
    val p_source_dir: String = "p_source_dir"
    val p_output_dir: String = "p_output_dir"
    val p_cache_dir: String = "p_cache_dir"

}
