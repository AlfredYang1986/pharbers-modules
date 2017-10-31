package com.pharbers.panel.util.csv

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 17-10-23.
  */
trait memory_size extends PharbersInjectModule {
    override val id: String = "page-memory"
    override val configPath: String = "pharbers_config/page_memory.xml"
    override val md = "page-size" :: "buffer-size" :: Nil

    protected val page_size: Int = config.mc.find(p => p._1 == "page-size").get._2.toString.toInt
    protected val buffer_size: Int = config.mc.find(p => p._1 == "buffer-size").get._2.toString.toInt
}
