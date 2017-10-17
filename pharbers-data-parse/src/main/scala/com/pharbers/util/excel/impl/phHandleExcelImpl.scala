package com.pharbers.util.excel.impl

import com.pharbers.phData
import com.pharbers.util.excel.impl.phHandleExcelImpl.ExcelData
import com.pharbers.util.excel.phHandleExcelTrait

import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */

object phHandleExcelImpl{
    implicit val filterFun: Map[String,String] => Boolean = { _ => true }
    implicit val postFun: Map[String,String] => Option[Map[String,String]] = { tr => Some(tr) }
    case class ExcelData(file_local: String,
                         sheetId: Int = 1,
                         fieldArg: Map[String, String] = Map(),
                         defaultValueArg: Map[String, String] = Map(),
                         sheetName: String = "") extends phData
}

case class phHandleExcelImpl() extends phHandleExcelTrait {
    override def getCount(file_local: String): Int = {
        new phReadExcelHandle(file_local).getCount
    }

    override def readExcel(arg: ExcelData)
                 (implicit filterFun: Map[String,String] => Boolean,
                  postFun: Map[String,String] => Option[Map[String,String]]) = {

        new phReadExcelHandle(arg.file_local){
            override val fieldMap = arg.fieldArg
            override val defaultValueMap = arg.defaultValueArg

            override def processFun(): Option[Map[String, String]] = {
                var tr = titleMap.keys.map{ t => (titleMap(t), rowMap(t)) }.toMap
                if (filterFun(tr)) {
                    tr.foreach { x =>
                        x._2 match {
                            case s: String if s == "" => tr += x._1 -> getDefaultValue(x._1, tr)
                            case _: String => Unit // 必须有，处理超多后面空数据还持续读取问题
                            case _ => Unit
                        }
                    }
                    tr = postFun(tr).getOrElse(throw new Exception("parse xlsx error => postFun error"))
                    Some(tr)
                } else {
                    None
                }
            }
        }.process(arg.sheetId, arg.sheetName)
    }

    override def writeByList(content: List[Map[String, Any]], output_file: String,
                    sheet: String = "Sheet1", cellNumArg: Map[String, Int] = Map()) = {
        def getWriteSeq(row: Map[String, Any]): Map[String, Int] ={
            var i = -1
            row.map{x =>
                i += 1
                x._1 -> i
            }
        }
        implicit val writeSeq = cellNumArg.size match {
            case 0 => getWriteSeq(content.head)//初始化默认顺序
            case _ => cellNumArg
        }
        new phWriteExcelHandle(output_file).write(content, sheet)
    }
}