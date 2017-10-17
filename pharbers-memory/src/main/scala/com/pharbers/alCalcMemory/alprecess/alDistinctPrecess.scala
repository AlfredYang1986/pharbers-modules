package com.pharbers.alCalcMemory.alprecess

import com.pharbers.ErrorCode._
import com.pharbers.alCalcMemory.aldata.alStorage
import com.pharbers.alCalcMemory.alOther.alException.alException
import com.pharbers.alCalcMemory.alstages.{alInitStage, alMemoryStage, alPresisStage, alStage}

/*
 * 　Modify by clock on 05/06/2017.
 */
class alDistinctPrecess extends alPrecess {
    def precess(j: alStage): List[alStage] = {
        try {
            j match {
                case _: alInitStage => {
                    alException(errorToJson("not memory stage cannot precess"))
                    Nil
                }
                case _: alPresisStage => {
                    alException(errorToJson("not memory stage cannot precess"))
                    Nil
                }
                case _: alMemoryStage => {
                    val ns = j.storages.map { x =>
                        x.asInstanceOf[alStorage].distinct
                    }
                    alStage(ns) :: Nil
                }
            }
        } catch {
            case ex: OutOfMemoryError => alException(errorToJson("not enough memory")); throw ex
            case ex: Exception => alException(errorToJson("unknow error")); throw ex
        }
    }
}