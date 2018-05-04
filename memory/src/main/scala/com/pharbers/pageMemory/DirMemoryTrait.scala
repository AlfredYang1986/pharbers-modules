package com.pharbers.pageMemory

import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.pageMemory.fop.dir.dirFlushTrait

trait DirMemoryTrait extends PharbersInjectModule {
    override val id: String = "flush-memory"
    override val configPath: String = "pharbers_config/flush_memory.xml"
    override val md = "buffer-size" :: "max-file-size" :: Nil

    val buffer_size = config.mc.find(p => p._1 == "buffer-size").get._2.toString.toInt
    val max_file_size = config.mc.find(p => p._1 == "max-file-size").get._2.toString.toInt

    case class dirFlushImpl(override val path : String,
                            override val bufferSize : Int,
                            override val maxFileSize : Int) extends dirFlushTrait

    val path : String
    lazy val dl = dirFlushImpl(path, buffer_size, max_file_size)

    def appendLine(l : String) = dl.appendLine(l)
    def appendArrByte(arr_byte : Array[Byte]) = dl.appendArrByte(arr_byte)
    def close = dl.closeFlush
}

case class dirFlushMemory(override val path : String) extends DirMemoryTrait