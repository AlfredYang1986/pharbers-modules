package com.pharbers.panel.util.csv

import com.pharbers.panel.util.phDataHandle

import scala.collection.immutable.Map

/**
  * Created by clock on 17-9-7.
  */
trait phHandleCsvTrait extends phDataHandle{
    def writeByList(content: List[Map[String, Any]], output_file: String)
                   (implicit titleSeq: List[String] = List()) : List[String]
    def appendByLine(line: Map[String, Any])
                    (implicit titleSeq: List[String], output_file: String) : String
}
