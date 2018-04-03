package com.pharbers.panel.nhwa

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-9-21.
  */
object NhwaFilePath extends PharbersInjectModule {
    override val id: String = "match_file_config"
    override val configPath: String = "src/scala/com/pharbers/panel/nhwa/match_file_config.xml"
    override val md = "product_match_file" :: "markets_match_file" ::
                    "not_published_hosp_file" :: "fill_hos_data_file" :: Nil

    val p_product_match_file: String = config.mc.find(p => p._1 == "product_match_file").get._2.toString
    val p_markets_match_file: String = config.mc.find(p => p._1 == "markets_match_file").get._2.toString
    val p_not_published_hosp_file: String = config.mc.find(p => p._1 == "not_published_hosp_file").get._2.toString
    val p_fill_hos_data_file: String = config.mc.find(p => p._1 == "fill_hos_data_file").get._2.toString
}
