package com.pharbers.util.csv.impl

import java.io.FileWriter

import com.pharbers.util.csv.phHandleCsvTrait

import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */
case class phHandleCsvImpl() extends phHandleCsvTrait {
    override def writeByList(content: List[Map[String, Any]], output_file: String,
                             titleSeqArg: List[String] = List()) = {
        if(content.isEmpty) throw new Exception("写入的数据为空")

        val titleSeq = if(titleSeqArg.isEmpty){
            content.head.keys.toList
        }else{
            titleSeqArg
        }

        val out = new FileWriter(output_file)

        val result = content.map{map =>
            val temp = titleSeq.map{ t =>
                map(t).toString
            }
            val line = temp.mkString(",") +"\r\n"
            out.write(line)
            line
        }
        out.close()
        result
    }
}