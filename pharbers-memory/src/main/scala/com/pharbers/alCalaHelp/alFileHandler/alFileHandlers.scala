package com.pharbers.alCalaHelp.alFileHandler

import com.pharbers.alCalcMemory.aldata.alStorage
import com.pharbers.aqll.common.alFileHandler.alFileHandler

/**
  * Created by Alfred on 09/03/2017.
  */
trait alFileHandlers extends alFileHandler{
    def sync(path : String, s : alStorage, f : Option[String]) = Unit
}