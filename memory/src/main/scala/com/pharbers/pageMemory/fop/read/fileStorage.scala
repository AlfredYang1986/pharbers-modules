package com.pharbers.pageMemory.fop.read

import java.io.{File, RandomAccessFile}
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

trait fileStorage extends fileStorageTrait {

    val path : String
    val bufferSize : Int
    val pageSize : Int

    val chl: Byte = '\n'.toByte
    lazy val raf: RandomAccessFile = new RandomAccessFile(new File(path), "r")
    lazy val fc: FileChannel = raf.getChannel
    lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, raf.length)

//    var position = 0

    override def closeStorage = {
        fc.close()
        raf.close()
    }

    override def seekToPage(page : Int) : Long = {
        mem.position(0)
//        position = 0
        var result = 0
        (0 to page) foreach { _ =>
            val length = math.min(pageSize, fileLength - mem.position.toLong)
            result = nextPage
            mem.position((result + length).toInt)
        }

        result = adjustPositionAcc(result - 1)
        mem.position(result)
//        position = result

        result
    }

    def adjustPositionAcc(cur : Int) : Int = {
        if (cur < 0) 0
        else {
            mem.position(cur)
            if (mem.get == chl) cur + 1
            else adjustPositionAcc(cur - 1)
        }
    }

    def nextPage : Int = {
        val pos = mem.position // page * pageSize
        val result = adjustPositionAcc(pos - 1)
        mem.position(result)
        result
    }

    override def capCurrentPage(pg : Int, buf : Array[Byte]) : Long = {
        val result = math.min(buf.length, fileLength - mem.position.toLong)
        mem.get(buf, 0, result.toInt)
        result
    }

    lazy val fileLength = raf.length
    override def pageCount : Int = (fileLength / pageSize + 1).toInt
    override def hasNextPage: Boolean = ???
}
