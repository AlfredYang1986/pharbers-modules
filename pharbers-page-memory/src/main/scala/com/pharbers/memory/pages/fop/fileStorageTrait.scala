package com.pharbers.memory.pages.fop

trait fileStorageTrait {
    def seekToPage(page: Int) : Long
    def capCurrentPage(pg: Int, buf: Array[Byte]) : Long
    def pageCount : Int
    def closeStorage
}
