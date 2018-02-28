package com.pharbers.panel.util

import scala.collection.immutable.Map
import com.pharbers.panel.util.csv.phHandleCsv
import com.pharbers.panel.util.excel.{phExcelFileInfo, phHandleExcel}

/**
  * Created by clock on 18-2-27.
  */
trait phExcel2Csv {
    def excel2Csv(excel: phExcelFileInfo, output_file: String)
                 (implicit filterFun: Map[String, String] => Boolean = _ => true,
                  postFun: Map[String, String] => Option[Map[String, String]] = tr => Some(tr)) = {
        val excelHandle = phHandleExcel()
        val csvHandle = phHandleCsv()
        var title: Map[String, String] = Map()

        val processFun: Map[String, String] => Unit = { row =>
            if (title.isEmpty) title = row.map{ x => (x._1, excel.fieldArg.getOrElse(x._2, x._2))}
            else {
                val temp = title.map { x => (x._2, row.getOrElse(x._1, "")) }
                val data = temp.map { x =>
                    if (x._2 == "") (x._1, excelHandle.setDefaultValue(x._1, temp, excel))
                    else x
                }
                if(filterFun(data)){
                    csvHandle.appendByLine(postFun(data).getOrElse(throw new Exception("data parse error => postFun error")), output_file)
                }
            }
        }

        excelHandle.callbackReading(excel)(processFun)
    }
}
