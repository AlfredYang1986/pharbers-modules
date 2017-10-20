package com.pharbers.panel.util.csv

import java.io.{File, RandomAccessFile}
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.memory.pages.fop.fileStorage

/**
  * Created by clock on 17-10-19.
  */
trait phFileWriteStorage extends PharbersInjectModule with fileStorage {
    override val id: String = "page-memory"
    override val configPath: String = "pharbers_config/page_memory.xml"
    override val md = "page-size" :: "buffer-size" :: Nil

    override val path: String
    override lazy val raf: RandomAccessFile = new RandomAccessFile(new File(path), "rws")
    override lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, raf.length)

    override val pageSize: Int = config.mc.find(p => p._1 == "page-size").get._2.toString.toInt
    override val bufferSize: Int = config.mc.find(p => p._1 == "buffer-size").get._2.toString.toInt
}

case class phFileWriteStorageImpl(override val path: String) extends phFileWriteStorage