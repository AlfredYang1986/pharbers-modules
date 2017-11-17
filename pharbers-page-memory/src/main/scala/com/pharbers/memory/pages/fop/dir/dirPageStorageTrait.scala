package com.pharbers.memory.pages.fop.dir

import java.io.File

import com.pharbers.memory.pages.{pageMemory2, pageMemory3}

trait dirPageStorageTrait {
    val path : String

    def readAllData(func : String => Unit) : Unit = {
        val dir = new File(path)
        val files = dir.list().filter(x => x.startsWith("data"))

        files.foreach { f =>
            val file = pageMemory3(path + "/" + f)
//            (0 until file.pageCount.toInt) foreach { i =>
            file.allData (line => line.foreach(x => func(x)))
            file.closeStorage
        }
    }
}

case class dirPageStorage(override val path : String) extends dirPageStorageTrait
