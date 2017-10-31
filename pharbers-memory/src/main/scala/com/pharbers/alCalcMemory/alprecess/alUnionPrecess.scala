package com.pharbers.alCalcMemory.alprecess

import com.pharbers.ErrorCode._
import com.pharbers.alCalcMemory.aldata.alStorage
import com.pharbers.alCalcMemory.alOther.alException.alException
import com.pharbers.alCalcMemory.alstages.{alInitStage, alMemoryStage, alPresisStage, alStage}


class alUnionPrecess extends alPrecess {
    def precess(j : alStage) : List[alStage] = {

        try {
            j match {
                case _ : alInitStage => alException(errorToJson("not memory stage cannot precess"));Nil
                case _ : alPresisStage => alException(errorToJson("not memory stage cannot precess"));Nil
                case _ : alMemoryStage => 
                    alStage(alStorage.combine((j.storages.asInstanceOf[List[alStorage]])) :: Nil) :: Nil
            }

        } catch {
            case ex : OutOfMemoryError => alException(errorToJson("not enough memory")); throw ex
            case ex : Exception => alException(errorToJson("unknow error")); throw ex
        }
    }
}