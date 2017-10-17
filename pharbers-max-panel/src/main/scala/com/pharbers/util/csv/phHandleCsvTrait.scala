package com.pharbers.util.csv

import com.pharbers.phDataHandle
import com.pharbers.util.excel.impl.phHandleExcelImpl.ExcelData

import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */
trait phHandleCsvTrait extends phDataHandle{
    def writeByList(content: List[Map[String, Any]], output_file: String,
                    titleSeq: List[String] = List()) : List[String]
}
