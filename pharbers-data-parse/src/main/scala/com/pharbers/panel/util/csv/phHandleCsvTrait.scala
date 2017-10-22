package com.pharbers.panel.util.csv

import java.io.{File, FileWriter, RandomAccessFile}
import java.util.UUID

import com.pharbers.memory.pages.fop.{fileStorage, pageStorage}
import com.pharbers.panel.util.phDataHandle

import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */
case class phHandleCsvImpl() extends phHandleCsvTrait with phSortInsertCsvTrait
case class pageStorageImpl(override val pageSize: Int)(implicit override val fs: fileStorage) extends pageStorage

trait phHandleCsvTrait extends phDataHandle {

    def writeByList(content: List[Map[String, Any]], output_file: String)
                   (implicit titleSeqArg: List[String] = List()) : List[String] = {
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

    def appendByLine(line: Map[String, Any], output_file: String)
                    (implicit titleSeqArg: List[String]) : String = {
        if(line.isEmpty) throw new Exception("写入的数据为空")

        val titleSeq = if(titleSeqArg.isEmpty){
            line.keys.toList
        }else{
            titleSeqArg
        }

        val lineStr = titleSeq.map(line(_).toString).mkString(spl) + chl

        val file = new File(output_file)
        createFile(file)
//        file.deleteOnExit // JVM退出自动删除

        val out = new RandomAccessFile(output_file, "rw")
        out.seek(out.length)
        out.write(lineStr.getBytes)
        out.close()

        lineStr
    }

}

trait phSortInsertCsvTrait extends phDataHandle {

    def sortInsert(line: Map[String, Any], output_file_arg: List[String],
                   sortFun: (Map[String, Any], Map[String, Any]) => Int,
                   sameLineFun: List[Map[String, Any]] => (Map[String, Any], Map[String, Any]))
                  (implicit titleSeqArg: List[String], base_local: String): String = {
        if (line.isEmpty) throw new Exception("write data is null")

        val output_lst = output_file_arg match {
            case Nil => base_local + UUID.randomUUID.toString :: Nil
            case _ => output_file_arg
        }

        var ifs = phFileWriteStorageImpl(output_lst.last)
        def insertLine(pos: Int) = {
            val newLineArr = (titleSeqArg.map(line(_).toString).mkString(comma) + chl).getBytes

            if (ifs.raf.length + newLineArr.length > ifs.bufferSize)
                throw new Exception("size is over 300k")

            val tailSize = ifs.raf.length.toInt - pos
            val buf = new Array[Byte](tailSize)
            ifs.mem.position(pos)
            ifs.mem.get(buf, 0, tailSize)

            ifs.raf.seek(pos)
            ifs.raf.write(newLineArr ++ buf)
        }
        def sameLine(curLine: Map[String, Any], h_pos: Int, l_pos: Int,
                     sameLineFun: List[Map[String, Any]] => (Map[String, Any], Map[String, Any])) = {
            val newLineArr = sameLineFun(curLine :: line :: Nil) match {
                case (a, b) if b.isEmpty =>
                    (titleSeqArg.map(a(_).toString).mkString(comma) + chl).getBytes
                case (a, b) =>
                    (titleSeqArg.map(a(_).toString).mkString(comma) + chl).getBytes ++
                            (titleSeqArg.map(b(_).toString).mkString(comma) + chl).getBytes
            }

            val tailSize = ifs.raf.length.toInt - l_pos
            val buf = new Array[Byte](tailSize)
            ifs.mem.position(l_pos)
            ifs.mem.get(buf, 0, tailSize)

            ifs.raf.seek(h_pos)
            ifs.raf.write(newLineArr ++ buf)
        }

        def bodyFileFun(ps: pageStorageImpl) = {
            try {
                ps.allData.foreach { x =>
                    val cur = titleSeqArg.zip(x.split(comma).toList).toMap
                    val compare_result = sortFun(line, cur)

                    compare_result match {
                        case 0 =>
                            sameLine(cur, ps.line_head, ps.line_last, sameLineFun)
                            throw new Exception("break")
                        case _ => Unit
                    }
                }
            } catch {
                case ex: Exception if ex.getMessage == "break" => Unit
            }
        }
        def lastFileFun(ps: pageStorageImpl): String = {
            ps.allData match {
                case Stream() =>
                    insertLine(0)
                    ""
                case data: Stream[String] =>
                    try {
                        var pos = -1
                        data.foreach { x =>
                            val cur = titleSeqArg.zip(x.split(comma).toList).toMap
                            val compare_result = sortFun(line, cur)

                            compare_result match {
                                case 0 =>
                                    sameLine(cur, ps.line_head, ps.line_last, sameLineFun)
                                    throw new Exception("break")
                                case 1 =>
                                    pos = ps.line_last
                                case -1 => {
                                    insertLine(ps.line_head)
                                    throw new Exception("break")
                                }
                            }
                        }
                        insertLine(pos)
                        ""
                    } catch {
                        case ex: Exception if ex.getMessage == "break" => ""
                        case ex: Exception if ex.getMessage == "size is over 300k" =>
                            val new_file = base_local + UUID.randomUUID.toString
                            ifs = phFileWriteStorageImpl(new_file)
                            insertLine(0)
                            new_file
                    }
            }
        }

        output_lst.map { f =>
            val wfs = phFileWriteStorageImpl(f)
            val ps = pageStorageImpl(wfs.pageSize)(wfs)
            if (f == output_lst.last) {
                val new_file = lastFileFun(ps)
                wfs.closeStorage
                if(new_file == "") f else new_file
            } else {
                bodyFileFun(ps)
                wfs.closeStorage
                f
            }
        }.last
    }
}