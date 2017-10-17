package com.pharbers.util.excel

import com.pharbers.util.excel.impl.phHandleExcelImpl.ExcelData
import com.pharbers.util.phDataHandle

import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */
trait phHandleExcelTrait extends phDataHandle{
    def getCount(file_local: String): Int
    def readExcel(arg: ExcelData)
                 (implicit filterFun: Map[String,String] => Boolean,
                  postFun: Map[String,String] => Option[Map[String,String]]): List[Map[String, String]]
    def writeByList(content: List[Map[String, Any]], output_file: String,
                    sheet: String = "Sheet1", cellNumArg: Map[String, Int] = Map()): Boolean
}
