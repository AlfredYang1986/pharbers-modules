package com.pharbers.util.excel

import com.pharbers.phDataHandle
import scala.collection.mutable

/**
  * Created by clock on 17-9-7.
  */
trait phHandleExcelTrait extends phDataHandle{
    def getCount(file_local: String): Int
    def readToDB(file_local: String,
                 collection_name: String,
                 sheetId: Int = 1,
                 sheetName: String = "",
                 fieldArg: Map[String, String] = Map(),
                 defaultValueArg: Map[String, String] = Map())
                (implicit filterFun: (Map[String,String]) => Boolean,
                 postFun: (Map[String,String]) => Option[Map[String,String]]): List[Map[String, String]]
    def readToList(file_local: String, sheetId: Int = 1, sheetName: String = ""): List[Map[String, String]]
    def writeByList(output_file: String, content: List[Map[String, String]]): Boolean
}
