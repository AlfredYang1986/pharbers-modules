package com.pharbers.panel.util.csv.impl

import java.io.{File, FileWriter, RandomAccessFile}
import com.pharbers.panel.util.csv.phHandleCsvTrait
import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */
case class phHandleCsvImpl() extends phHandleCsvTrait {
    val spl = 31.toChar.toString
    val chl = "\n"

    override def writeByList(content: List[Map[String, Any]], output_file: String)
                            (implicit titleSeqArg: List[String] = List()): List[String] = {
        if(content.isEmpty) throw new Exception("写入的数据为空")

        val titleSeq = if(titleSeqArg.isEmpty){
            content.head.keys.toList
        }else{
            titleSeqArg
        }

        val file = new File(output_file)
        createFile(file)

        val out = new FileWriter(file)

        val result = content.map{map =>
            val temp = titleSeq.map{ t =>
                map(t).toString
            }
            val line = temp.mkString(spl) + chl
            out.write(line)
            line
        }
        out.close()
        result
    }

    override def appendByLine(line: Map[String, Any])
                             (implicit titleSeqArg: List[String], output_file: String): String = {
        if(line.isEmpty) throw new Exception("写入的数据为空")

        val titleSeq = if(titleSeqArg.isEmpty){
            line.keys.toList
        }else{
            titleSeqArg
        }

        val lineStr = titleSeq.map(line(_).toString).mkString(spl) + chl

        val file = new File(output_file)
        createFile(file)

        val out = new RandomAccessFile(output_file, "rw")
        out.seek(out.length)
        out.write(lineStr.getBytes)
        out.close()

        lineStr
    }

    private def createFile(file: File) = {
        if(!file.getParentFile.exists()) {
            if(!file.getParentFile.mkdirs()) {
                println("创建目标所在目录失败！")
            }
        }
        if(!file.exists())
            file.createNewFile
    }
}

