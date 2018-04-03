package com.pharbers.pageMemory

import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.pageMemory.fop.read.{fileStorage3, fileStorageTrait, pageStorage2}

trait PageMemoryTrait3 extends PharbersInjectModule {
    override val id: String = "page-memory"
    override val configPath: String = "pharbers_config/page_memory.xml"
    override val md = "page-size" :: "buffer-size" :: Nil

    val page_size = config.mc.find(p => p._1 == "page-size").get._2.toString.toInt
    val buffer_size = config.mc.find(p => p._1 == "buffer-size").get._2.toString.toInt

    case class fileStorageImpl(override val path : String,
                               override val bufferSize: Int,
                               override val pageSize: Int) extends fileStorage3

    case class pageStorageImpl(override val pageSize: Int)(implicit override val fs: fileStorageTrait) extends pageStorage2

    val path : String
    lazy val ps = pageStorageImpl(page_size)(fileStorageImpl(path, buffer_size, page_size))

    def allData(f : Stream[String] => Unit) = ps.allData(f)
    def closeStorage = ps.fs.closeStorage
}

case class pageMemory3(override val path : String) extends PageMemoryTrait3