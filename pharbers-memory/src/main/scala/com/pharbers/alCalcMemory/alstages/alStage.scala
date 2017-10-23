package com.pharbers.alCalcMemory.alstages

import com.pharbers.alCalcMemory.aldata.alStorage
import com.pharbers.ErrorCode._
import com.pharbers.alCalcMemory.alOther.alException.alException

/**
  * Created by Alfred on 10/03/2017.
  * 　Modify by clock on 05/06/2017.
  */

object alStage {
    def apply() : alStage = new alInitStage
    def apply(path :  String) : alStage = {
        val tmp = new alPresisStage
        tmp.storages = path :: Nil
        tmp
    }
    def apply(files : List[String]) : alPresisStage = {
        val tmp = new alPresisStage
        tmp.storages = files
        tmp
    }

    def apply(data : List[alStorage]): alMemoryStage = {
        val tmp = new alMemoryStage
        tmp.storages = data
        tmp
    }
}

trait alStage {
    var storages : List[AnyRef] = Nil
    def isCalc = false

    def canLength : Boolean = false
    def length : Int = {
        alException(errorToJson("only Memory can calc length"))
        -1
    }
}

class alInitStage extends alStage
class alMemoryStage extends alStage {
    override def canLength : Boolean = true
    override def isCalc: Boolean = storages.find (x => !x.asInstanceOf[alStorage].isCalc) == None
    override def length: Int = storages.map (x => x.asInstanceOf[alStorage].length).sum
}
class alPresisStage extends alStage
