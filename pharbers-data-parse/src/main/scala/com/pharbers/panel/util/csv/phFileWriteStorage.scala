package com.pharbers.panel.util.csv

import java.io.{File, RandomAccessFile}
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import com.pharbers.memory.pages.fop.{fileStorage, pageStorage}

/**
  * Created by clock on 17-10-19.
  */
trait phFileWriteStorage extends memory_size with fileStorage {
    override val path: String
    override lazy val raf: RandomAccessFile = new RandomAccessFile(new File(path), "rws")
    override lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, raf.length)

    override val pageSize: Int = page_size
    override val bufferSize: Int = buffer_size
}

case class phFileWriteStorageImpl(override val path: String) extends phFileWriteStorage
case class pageStorageImpl(override val pageSize: Int)(implicit override val fs: fileStorage) extends pageStorage
