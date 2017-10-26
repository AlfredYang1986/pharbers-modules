package com.pharbers.alCalcMemory.alOther.alFileHandler

import com.pharbers.alCalcMemory.aldata.alStorage
import scala.collection.mutable.ListBuffer

trait alFileHandler {
    def prase(path : String)(x : Any) : Any = null
    def write: Unit = null
    var data : ListBuffer[Any] = ListBuffer()
}

trait alFileHandlers extends alFileHandler{
    def sync(path : String, s : alStorage, f : Option[String]) = Unit
}