package com.pharbers.memory.pages

import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.memory.pages.fop.{fileFlushTrait, fileStorage, pageStorage}

trait FlushMemoryTrait extends PharbersInjectModule {
    override val id: String = "flush-memory"
    override val configPath: String = "pharbers_config/flush_memory.xml"
    override val md = "buffer-size" :: Nil

    val buffer_size = config.mc.find(p => p._1 == "buffer-size").get._2.toString.toInt

    case class flushImpl(override val path,
                         override val bufferSize) extends fileFlushTrait

    val path : String
    lazy val fl = flushImpl(path, buffer_size)

    def pushLine(l : String) = fl.pushLine(l)
}

case class flushMemory(override val path : String) extends FlushMemoryTrait
