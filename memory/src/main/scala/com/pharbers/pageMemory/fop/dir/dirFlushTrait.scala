package com.pharbers.pageMemory.fop.dir

import com.pharbers.pageMemory.fop.write.fileFlushTrait


trait dirFlushTrait {

    val path : String       // dir
    val bufferSize : Int
    val maxFileSize : Int

    var cur_index = 0
    var fileName = path + "/data" + cur_index
    def nextFileName = {
        cur_index = cur_index + 1
        fileName = path + "/data" + cur_index
        fileName
    }
    def nextFile = {
        cur_file.closeFlush
        cur_file = flushImpl(nextFileName, bufferSize, maxFileSize)
    }

    case class flushImpl(override val path : String,
                         override val bufferSize : Int,
                         override val maxFileSize : Int) extends fileFlushTrait

    var cur_file : flushImpl = flushImpl(fileName, bufferSize, maxFileSize)

    def closeFlush = {
        cur_file.closeFlush
    }

    def appendLine(line : String) : Unit = {
        try {
            cur_file.appendLine(line)

        } catch {
            case _ : Exception => {
                nextFile
                appendLine(line)
            }
        }

    }

    def appendArrByte(arr_byte : Array[Byte]) : Unit = {
        try {
            cur_file.appendArrByte(arr_byte)

        } catch {
            case _ : Exception => {
                nextFile
                appendArrByte(arr_byte)
            }
        }
    }
}
