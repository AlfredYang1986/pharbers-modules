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
    implicit val postFun: (Map[String,String]) => Option[Map[String,String]] = { tr => Some(tr) }
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
                          postFun: (Map[String,String]) => Option[Map[String,String]]) = {

        new phReadExcelHandle(file_local){
            override val fieldMap = fieldArg
            override val defaultValueMap = defaultValueArg

            override def processFun(): Option[Map[String, String]] = {
                var tr = titleMap.keys.map{t => (titleMap(t),rowMap(t)) }.toMap

                if (filterFun(tr)) {
                    tr.map { x =>
                        x._2 match {
                            case s: String if s == "" => tr += x._1 -> getDefaultValue(x._1, tr)
                            case s: String => Unit
                            case _ => Unit
                        }
                    }

                    tr = postFun(tr).getOrElse(Map())

                    val builder = MongoDBObject.newBuilder
                    tr.keys.foreach{x =>
                        builder += x -> tr(x)
                    }
                    _data_connection.getCollection(collection_name) += builder.result()
                    Some(tr)
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