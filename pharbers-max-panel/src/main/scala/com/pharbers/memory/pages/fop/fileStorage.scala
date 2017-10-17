package com.pharbers.memory.pages.fop

import java.io.{File, RandomAccessFile}
import java.nio.channels.FileChannel

trait fileStorage {

    val path : String
    val bufferSize : Int
    val pageSize : Int

    val chl = '\n'.toByte
    lazy val raf = new RandomAccessFile(new File(path), "r")
    lazy val fc = raf.getChannel()
    lazy val mem = fc.map(FileChannel.MapMode.READ_ONLY, 0, raf.length())

    def closeStorage = fc.close()

    def adjustPositionAcc(cur : Int) : Int = {
        if (cur < 0) 0
        else {
            mem.position(cur)
            if (mem.get() == chl) cur + 1
            else adjustPositionAcc(cur - 1)
        }
    }

    def seekToPage(page : Int) : Int = {
        mem.position(0)
        var result = 0
        for (pg <- 0 to (page - 1)) {
            val length = math.min(pageSize, fileLength.toInt - mem.position())
            result = nextPage
            mem.position(result + length)
        }

        result
    }

    def nextPage : Int = {
        val pos = mem.position() // page * pageSize
        val result = adjustPositionAcc(pos - 1)
        mem.position(result)
        result
    }

    def capCurrentPage(pg : Int, buf : Array[Byte]) : Int = {
        val result = math.min(buf.length, fileLength.toInt - mem.position())
        mem.get(buf, 0, result)
        result
    }

    def fileLength = raf.length()
    val pageCount = fileLength / pageSize + 1
}
