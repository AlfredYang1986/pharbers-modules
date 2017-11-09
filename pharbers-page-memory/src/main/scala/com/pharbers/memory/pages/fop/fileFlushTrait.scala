package com.pharbers.memory.pages.fop

import java.io.{File, RandomAccessFile}
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets

trait fileFlushTrait {

    val path : String
    val bufferSize : Int

    var seek = 0
    var mark = 0
    lazy val buf : Array[Byte] = new Array[Byte](bufferSize)
    lazy val raf : RandomAccessFile = new RandomAccessFile(new File(path), "rw")
    lazy val fc: FileChannel = raf.getChannel

    def closeFlush = {
        flush
        fc.close()
        raf.close()
    }

    def appendLine(line : String) : Unit = {
        if (!line.endsWith("\n")) appendLine(line + "\n")
        else {
            val in = line.getBytes(StandardCharsets.UTF_8)
            if (seek + in.length > bufferSize)
                flush

            in.copyToArray(buf, seek, in.length)
            seek += in.length
        }
    }

    def flush = {
        lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, mark, seek)
        mem.put(buf, 0, seek)

        seek = 0
        mark += seek
    }
}
