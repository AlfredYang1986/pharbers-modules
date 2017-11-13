package com.pharbers.memory.pages.fop

import java.io.{File, RandomAccessFile}
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets

trait fileStorage2 extends fileStorageTrait {

    val path : String
//    val bufferSize : Int
    val pageSize : Int
    val stepSize : Int
    lazy val breaks : Stream[Long] = {
        def calcNextPagebreak(cur : Long) : Long = {
            val start = math.max(cur - stepSize, 0)
            println(s"start point is : $start")

            if (start < raf.length){
                val step_mem: MappedByteBuffer =
                    fc.map(FileChannel.MapMode.READ_ONLY, start, math.min(stepSize, raf.length() - start))

                def adjustPositionAcc2(pos : Int) : Long = {
                    if (pos < 0) 0
                    else {
                        step_mem.position(pos)
                        if (step_mem.get == chl) pos + 1
                        else adjustPositionAcc2(pos - 1)
                    }
                }

                val result = start + adjustPositionAcc2((cur - start - 1).toInt)
                println(s"result is : $result")
                result

            } else -1
        }

        def pageBreaks(last : Long) : Stream[Long] = {
            println(s"file size is : ${raf.length}")
            if (last < 0) Stream.empty
            else last #:: pageBreaks(calcNextPagebreak(last + pageSize))
        }

        pageBreaks(0)
    }

    val chl: Byte = '\n'.toByte
    lazy val raf: RandomAccessFile = new RandomAccessFile(new File(path), "r")
    lazy val fc: FileChannel = raf.getChannel

    var position = 0

    def closeStorage = {
        fc.close()
        raf.close()
    }

    override def seekToPage(page : Int) : Long = {
        // return start pos
        breaks(page)
    }

    override def capCurrentPage(pg : Int, buf : Array[Byte]) : Long = {
        // return cap length
        val start_pos = seekToPage(pg)
        val result = math.min(pageSize, fileLength - start_pos)
        val mem : MappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, start_pos, result)
        mem.get(buf, 0, result.toInt)
        result
    }

    lazy val fileLength = raf.length
    override def pageCount : Int = {
        assert(breaks.length == fileLength / pageSize + 1)
        (fileLength / pageSize + 1).toInt
    }

    def for_test(pos : Int, length : Int) : String = {
        lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, raf.length)
        lazy val buf : Array[Byte] = new Array[Byte](pageSize)
        mem.get(buf, 0, 20)

        new String(buf, StandardCharsets.UTF_8)
    }
}
