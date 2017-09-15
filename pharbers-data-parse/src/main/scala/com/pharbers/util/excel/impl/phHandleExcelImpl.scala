package com.pharbers.util.excel.impl

import com.mongodb.casbah.commons.MongoDBObject
import com.pharbers.mongodbConnect._data_connection
import com.pharbers.util.excel.phHandleExcelTrait

import scala.collection.mutable

/**
  * Created by clock on 17-9-7.
  */
object phHandleExcelImpl{
    implicit val filterFun: (Map[String,String]) => Boolean = { tr => true }
    implicit val postFun: (mutable.Map[String,String]) => Unit = { tr => Unit }
}

class phHandleExcelImpl extends phHandleExcelTrait {
    override def getCount(file_local: String): Int = {
        new phReadExcelHandle(file_local).getCount
    }

    override def readToDB(file_local: String,
                          collection_name: String,
                          sheetId: Int = 1,
                          sheetName: String = "",
                          fieldArg: Map[String, String] = Map(),
                          defaultValueArg: Map[String, String] = Map())
                         (implicit filterFun: (Map[String,String]) => Boolean,
                          postFun: mutable.Map[String,String] => Unit) = {

        new phReadExcelHandle(file_local){
            override val fieldMap = fieldArg
            override val defaultValueMap = defaultValueArg

            override def processFun(): Option[Map[String, String]] = {
                val tr = titleList.zip(rowList).toMap
                if (filterFun(tr)) {
                    val mutableTr =  mutable.Map.empty ++ tr
                    val flag = rowList.length
                    for (i <- titleList.indices if i < flag) {
                        rowList(i) match {
                            case s: String if s == "" => mutableTr += titleList(i) -> setDefaultValue(titleList(i), tr)
                            case s: String => s
                            case _ => throw new Exception("parse xlsx error")
                        }
                    }

                    postFun(mutableTr)

                    val builder = MongoDBObject.newBuilder
                    mutableTr.keys.foreach{x =>
                        builder += x -> mutableTr(x)
                    }
                    _data_connection.getCollection(collection_name) += builder.result()
                    Some(mutableTr.toMap)
                } else {
                    None
                }
            }
        }.process(sheetId, sheetName)
    }

    override def readToList(file_local: String,sheetId: Int = 1, sheetName: String = ""): List[Map[String, String]] = {
        new phReadExcelHandle(file_local).process(sheetId, sheetName)
    }

    override def writeByList(output_file: String, content: List[Map[String, String]]) = {
        new phWriteExcelHandle(output_file).write(content)
        true
    }
}