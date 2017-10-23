package com.pharbers.alCalcMemory.alprecess

import com.pharbers.alCalcMemory.alOther.alFileHandler.alFilesOpt.alFileOpt

class alRestoreGroupedPrecess extends alRestorePrecess {
    override def pathProxy(path : String) : String = {
        val tf = alFileOpt(path)
        if (tf.isDir) tf.listAllFiles.head
        else path
    }
}