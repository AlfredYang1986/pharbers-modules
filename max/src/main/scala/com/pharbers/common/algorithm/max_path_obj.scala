package com.pharbers.common.algorithm

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-9-21.
  */
object max_path_obj extends PharbersInjectModule {
    override val id: String = "panel_config"
    override val configPath: String = "pharbers_config/panel_config.xml"
    override val md = "client_dir" :: "match_file_dir" :: "cache_dir" :: "result_dir" :: Nil

    val p_clientPath: String = config.mc.find(p => p._1 == "client_dir").get._2.toString
    val p_matchFilePath: String = config.mc.find(p => p._1 == "match_file_dir").get._2.toString
    val p_cachePath: String = config.mc.find(p => p._1 == "cache_dir").get._2.toString
    val p_resultPath: String = config.mc.find(p => p._1 == "result_dir").get._2.toString
}
