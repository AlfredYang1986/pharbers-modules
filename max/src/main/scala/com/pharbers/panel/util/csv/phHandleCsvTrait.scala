//package com.pharbers.panel.util.csv
//
//import java.util.UUID
//
//import com.pharbers.panel.util.csv.handle.{pageStorageImpl, phFileWriteStorageImpl}
//import com.pharbers.panel2.util.phDataHandle
//
//import scala.collection.immutable.Map
//
///**
//  * Created by clock on 17-9-7.
//  */
//trait phSortInsertCsvTrait extends phDataHandle {
//    def sortInsert(line: Map[String, Any], output_file_arg: List[String],
//                   sortFun: (Map[String, Any], Map[String, Any]) => Int,
//                   sameLineFun: List[Map[String, Any]] => (Map[String, Any], Map[String, Any]))
//                  (implicit titleSeqArg: List[String], base_local: String): String = {
//        if (line.isEmpty) throw new Exception("write data is null")
//
//        def readPanelAcc(lst: List[String]): String = lst match {
//            case Nil => sort_insert(UUID.randomUUID.toString, base_local)
//            case one :: Nil =>  sort_insert(one, base_local)
//            case head :: tail =>
//                same_merge(head)
//                readPanelAcc(tail)
//        }
//
//        def sort_insert(file: String, base_local: String): String = {
//            val rfs = phFileWriteStorageImpl(base_local + file)
//            val ps = pageStorageImpl(rfs.pageSize)(rfs)
//
//            var name = file
//            ps.allData match {
//                case Stream() => insertLine(0, base_local + name)
//                case data: Stream[String] =>
//                    try {
//                        var pos = -1.toLong
//                        data.foreach { x =>
//                            val cur = titleSeqArg.zip(x.split(spl).toList).toMap
//                            val compare_result = sortFun(line, cur)
//
//                            compare_result match {
//                                case 0 =>
//                                    sameLine(cur, ps.line_head, ps.line_last, base_local + name, sameLineFun)
//                                    throw new Exception("break")
//                                case 1 =>
//                                    pos = ps.line_last
//                                case -1 => {
//                                    insertLine(ps.line_head, base_local + name)
//                                    throw new Exception("break")
//                                }
//                            }
//                        }
//                        insertLine(pos, base_local + name)
//                    } catch {
//                        case ex: Exception if ex.getMessage == "break" => Unit
//                        case ex: Exception if ex.getMessage == "size is over 300k" =>
//                            name = UUID.randomUUID.toString
//                            insertLine(0, base_local + name)
//                    }
//            }
//            rfs.closeStorage
//            name
//        }
//
//        def insertLine(pos: Long, file: String) = {
//            val ifs = phFileWriteStorageImpl(file)
//            val newLineArr = (titleSeqArg.map(line(_).toString).mkString(spl) + chl).getBytes
//
//            if (ifs.raf.length + newLineArr.length > ifs.bufferSize)
//                throw new Exception("size is over 300k")
//
//            val tailSize = ifs.raf.length.toInt - pos
//            val buf = new Array[Byte](tailSize.toInt)
//            ifs.mem.position(pos.toInt)
//            ifs.mem.get(buf, 0, tailSize.toInt)
//
//            ifs.raf.seek(pos)
//            ifs.raf.write(newLineArr ++ buf)
//            ifs.closeStorage
//        }
//
//        def sameLine(curLine: Map[String, Any], h_pos: Long, l_pos: Long, file: String,
//                     sameLineFun: List[Map[String, Any]] => (Map[String, Any], Map[String, Any])) = {
//            val ifs = phFileWriteStorageImpl(file)
//            var newLineArr = sameLineFun(curLine :: line :: Nil) match {
//                case (a, b) if b.isEmpty =>
//                    (titleSeqArg.map(a(_).toString).mkString(spl) + chl).getBytes
//                case (a, b) =>
//                    (titleSeqArg.map(a(_).toString).mkString(spl) + chl).getBytes ++
//                            (titleSeqArg.map(b(_).toString).mkString(spl) + chl).getBytes
//            }
//
/////            println("curLine + " + curLine)
/////            println("line + " + line)
//
//            //WARN : The input causes the pointer chaos
//            val reduced = (l_pos - h_pos - newLineArr.length).toInt
//            if(reduced > 0){
//                newLineArr = newLineArr.dropRight(1)
//                (0 until reduced) foreach { _ =>
//                    newLineArr ++= "0".getBytes
//                }
//                newLineArr ++= "\n".getBytes
//            }
//
//            val tailSize = ifs.raf.length.toInt - l_pos
//            val buf = new Array[Byte](tailSize.toInt)
//            ifs.mem.position(l_pos.toInt)
//            ifs.mem.get(buf, 0, tailSize.toInt)
//
//            ifs.raf.seek(h_pos)
//            ifs.raf.write(newLineArr ++ buf)
//            ifs.closeStorage
//        }
//
//        def same_merge(file: String) = {
//            val rfs = phFileWriteStorageImpl(file)
//            val ps = pageStorageImpl(rfs.pageSize)(rfs)
//
//            try {
//                ps.allData.foreach { x =>
//                    val cur = titleSeqArg.zip(x.split(spl).toList).toMap
//                    val compare_result = sortFun(line, cur)
//
//                    compare_result match {
//                        case 0 =>
//                            sameLine(cur, ps.line_head, ps.line_last, file, sameLineFun)
//                            throw new Exception("break")
//                        case _ => Unit
//                    }
//                }
//            } catch {
//                case ex: Exception if ex.getMessage == "break" => Unit
//            }
//
//            rfs.closeStorage
//        }
//
//        readPanelAcc(output_file_arg)
//    }
//}