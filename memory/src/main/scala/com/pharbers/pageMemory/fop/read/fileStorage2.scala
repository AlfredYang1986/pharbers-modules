package com.pharbers.pageMemory.fop.read

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

            if (cur < raf.length){
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

                val result = math.min(start + adjustPositionAcc2((cur - start - 1).toInt), raf.length)
                result

            } else -1
        }

        def pageBreaks(last : Long) : Stream[Long] = {
            if (last < 0) Stream.empty
            else last #:: pageBreaks(calcNextPagebreak(last + pageSize))
        }

        pageBreaks(0)
    }

    val chl: Byte = '\n'.toByte
    lazy val raf: RandomAccessFile = new RandomAccessFile(new File(path), "r")
    lazy val fc: FileChannel = raf.getChannel

    var position = 0

    override def closeStorage = {
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
//        println(s"file is $path")
//        println(s"breaks.length ${breaks.length}")
//        println(s"file Length ${fileLength / pageSize + 1}")
//        assert(breaks.length == fileLength / pageSize + 1)
//        (fileLength / pageSize + 1).toInt
        breaks.length
    }

    def for_test(pos : Int, length : Int) : String = {
        lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, raf.length)
        lazy val buf : Array[Byte] = new Array[Byte](pageSize)
        mem.get(buf, 0, 20)

        new String(buf, StandardCharsets.UTF_8)
    }

    override def hasNextPage: Boolean = ???
}
