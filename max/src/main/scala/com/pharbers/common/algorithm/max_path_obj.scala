package com.pharbers.common.algorithm

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-9-21.
  */
object max_path_obj extends PharbersInjectModule {
    override val id: String = "max_path_config"
    override val configPath: String = "pharbers_config/max_path_config.xml"
    override val md = "client_dir" :: "match_file_dir" :: "cache_dir" :: "panel_dir" :: "max_dir" :: "export_dir" :: Nil

    val p_clientPath: String = config.mc.find(p => p._1 == "client_dir").get._2.toString
    val p_matchFilePath: String = config.mc.find(p => p._1 == "match_file_dir").get._2.toString
    val p_cachePath: String = config.mc.find(p => p._1 == "cache_dir").get._2.toString
    val p_panelPath: String = config.mc.find(p => p._1 == "panel_dir").get._2.toString
    val p_maxPath: String = config.mc.find(p => p._1 == "max_dir").get._2.toString
    val p_exportPath: String = config.mc.find(p => p._1 == "export_dir").get._2.toString
}
