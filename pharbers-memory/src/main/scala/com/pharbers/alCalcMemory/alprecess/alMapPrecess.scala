package com.pharbers.alCalcMemory.alprecess

import com.pharbers.ErrorCode._
import com.pharbers.alCalcMemory.aldata.alStorage
import com.pharbers.alCalcMemory.alOther.alException.alException
import com.pharbers.alCalcMemory.alstages.{alInitStage, alMemoryStage, alPresisStage, alStage}

/**
  * Created by Alfred on 11/03/2017.
  * 　Modify by clock on 05/06/2017.
  */
class alMapPrecess(f : Any => Any) extends alPrecess {
    def precess(j : alStage) : List[alStage] = {

        try {
            j match {
                case _ : alInitStage => {
                    alException(errorToJson("not memory stage cannot precess"))
                    Nil
                }
                case _ : alPresisStage => {
                    alException(errorToJson("not memory stage cannot precess"))
                    Nil
                }
                case _ : alMemoryStage => {
                    val ns = j.storages.map { x =>
                        x.asInstanceOf[alStorage].map(f)
                    }
                    alStage(ns) :: Nil
                }
            }

        } catch {
            case ex : OutOfMemoryError => alException(errorToJson("not enough memory")); throw ex
            case ex : Exception => alException(errorToJson("unknow error")); throw ex
        }
    }
}