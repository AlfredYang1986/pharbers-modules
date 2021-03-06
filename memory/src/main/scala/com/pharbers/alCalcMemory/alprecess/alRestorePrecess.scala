package com.pharbers.alCalcMemory.alprecess

import com.pharbers.ErrorCode._
import com.pharbers.alCalcMemory.aldata.alStorage
import com.pharbers.alCalcMemory.alOther.alException.alException
import com.pharbers.alCalcMemory.alOther.alFileHandler.alText.alTextParser
import com.pharbers.alCalcMemory.alstages.{alInitStage, alMemoryStage, alPresisStage, alStage}

/**
  * Created by Alfred on 10/03/2017.
  * 　Modify by clock on 05/06/2017.
  */
class alRestorePrecess extends alPrecess{
    def pathProxy(path : String) : String = path
    
    def precess(j : alStage) : List[alStage] = {
        def precessAcc(path : String) : alStorage = alStorage(pathProxy(path), new alTextParser)

        try {
            j match {
                case it : alInitStage => alStage(precessAcc(it.storages.head.toString) :: Nil) :: Nil
                case it : alPresisStage => alStage(it.storages.map(x => precessAcc(x.toString))) :: Nil
                case _ : alMemoryStage => alException(errorToJson("memory stage cannot precess")); Nil
            }

        } catch {
            case ex : OutOfMemoryError => alException(errorToJson("not enough memory")); throw ex
            case ex : Exception => alException(errorToJson("unknow error")); throw ex
        }
    }
}