package com.pharbers.pageMemory

import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.pageMemory.fop.read.{fileStorage2, fileStorageTrait, pageStorage}

trait PageMemoryTrait2 extends PharbersInjectModule {
    override val id: String = "page-memory"
    override val configPath: String = "pharbers_config/page_memory.xml"
    override val md = "page-size" :: "buffer-size" :: Nil

    val page_size = config.mc.find(p => p._1 == "page-size").get._2.toString.toInt
    val buffer_size = config.mc.find(p => p._1 == "buffer-size").get._2.toString.toInt

    case class fileStorageImpl(override val path : String,
                               override val stepSize: Int,
                               override val pageSize: Int) extends fileStorage2

    case class pageStorageImpl(override val pageSize: Int)(implicit override val fs: fileStorageTrait) extends pageStorage

    val path : String
    lazy val ps = pageStorageImpl(page_size)(fileStorageImpl(path, 2048, page_size))

    def allLength : Int = allData.length
    def pageCount: Long = ps.pageCount

    def pageData(page : Int) : Stream[String] = {
        ps.cur_page = page
        ps.curInStorage
        ps.pageData
    }

    def allData : Stream[String] = ps.allData

    def closeStorage = ps.fs.closeStorage
}

case class pageMemory2(override val path : String) extends PageMemoryTrait2
