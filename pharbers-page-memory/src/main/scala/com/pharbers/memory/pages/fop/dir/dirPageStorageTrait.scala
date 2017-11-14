package com.pharbers.memory.pages.fop.dir

import java.io.File
import com.pharbers.memory.pages.pageMemory2

trait dirPageStorageTrait {
    val path : String

    def readAllData(func : String => Unit) : Unit = {
        val dir = new File(path)
        assert(dir.isDirectory)
        val files = dir.list().filter(x => x.startsWith("data"))

        files.foreach { f =>
            println(s"file name is $f")
            val file = pageMemory2(path + "/" + f)
            (0 until file.pageCount.toInt) foreach { i =>
                file.pageData(i).foreach (line => func(line))
            }
            file.closeStorage
        }
    }
}

case class dirPageStorage(override val path : String) extends dirPageStorageTrait
