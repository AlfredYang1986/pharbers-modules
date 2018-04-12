package com.pharbers.pageMemory.fop.write

import java.io.{File, RandomAccessFile}
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets

trait fileFlushTrait {

    val path : String
    val bufferSize : Int
    val maxFileSize : Int

    var seek = 0
//    var mark = 0
    lazy val buf : Array[Byte] = new Array[Byte](bufferSize)
    lazy val raf : RandomAccessFile = new RandomAccessFile(new File(path), "rw")
    lazy val fc : FileChannel = raf.getChannel

    def closeFlush = {
        flush
        fc.close()
        raf.close()
    }

    def appendLine(line : String) : Unit = {
        if (!line.endsWith("\n")) appendLine(line + "\n")
        else {
            val in = line.getBytes(StandardCharsets.UTF_8)

            if (raf.length() + seek + in.length > maxFileSize) {
                flush
                throw new Exception("extend max file size")
            }

            else if (seek + in.length > bufferSize) {
                flush
                appendLine(line)
            }

            else {
                in.copyToArray(buf, seek, in.length)
                seek += in.length
            }
        }
    }

    def appendArrByte(arr_byte : Array[Byte]) : Unit = {
        if (seek + arr_byte.length > bufferSize) {
            flush
            appendArrByte(arr_byte)
        }

        arr_byte.copyToArray(buf, seek, arr_byte.length)
        seek += arr_byte.length
    }

    def flush = {
        lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, raf.length(), seek)
        mem.put(buf, 0, seek)

        seek = 0
//        mark += seek
    }
}
