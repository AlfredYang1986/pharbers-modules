package com.pharbers.panel.util

import java.io.File

/**
  * Created by clock on 17-9-7.
  */
trait phData
trait phDataHandle {
    val spl = 31.toChar.toString
    val comma = ","
    val chl = "\n"

    def createFile(file: File) = {
        if(!file.getParentFile.exists()) {
            if(!file.getParentFile.mkdirs()) {
                println("创建目标所在目录失败！")
            }
        }
        if(!file.exists())
            file.createNewFile
    }
}
