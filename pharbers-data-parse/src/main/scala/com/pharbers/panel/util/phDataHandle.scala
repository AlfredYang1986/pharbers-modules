package com.pharbers.panel.util

import java.io.File
import play.api.libs.json.JsValue

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

trait phPanelHandle extends phDataHandle {
    def calcYM: JsValue
    def getMarkets: JsValue
    def getPanelFile(ym: List[String] = Nil, mkt: String = "", totalGenerateNum: Int = 0, curGenerateNum: Int = 0): JsValue
}