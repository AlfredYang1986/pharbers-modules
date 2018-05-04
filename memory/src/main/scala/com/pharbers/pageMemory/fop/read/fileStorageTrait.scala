package com.pharbers.pageMemory.fop.read

trait fileStorageTrait {
    def seekToPage(page: Int) : Long
    def capCurrentPage(pg: Int, buf: Array[Byte]) : Long
    def pageCount : Int
    def closeStorage
    def hasNextPage : Boolean
}
