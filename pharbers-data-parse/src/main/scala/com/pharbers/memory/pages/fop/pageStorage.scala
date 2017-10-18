package com.pharbers.memory.pages.fop

import java.nio.charset.StandardCharsets

trait pageStorage {

    val pageSize : Int
    val fs : fileStorage

    var cur : Int = 0

    val chl = '\n'.toByte
    var mark = -1
    var pos = -1
    var limit = -1

    lazy val buf : Array[Byte] = new Array[Byte](pageSize)

    def curInStorage : Array[Byte] = {
        fs.seekToPage(cur)
        limit = fs.capCurrentPage(cur, buf)
        mark = 0
        pos = 0

        buf
    }

    def nextLine : Option[String] = {
        def nextLineAcc(c : Int) : Array[Byte] = {
            if (buf(c) == chl) {
                val reVal = buf.slice(mark, c)

                mark = c + 1
                pos = c + 1

                reVal
            } else nextLineAcc(c + 1)
        }

        if (!isValidate) None
        else {
            try {
                Some(new String(nextLineAcc(pos), StandardCharsets.UTF_8))
            } catch {
                case _ : java.lang.ArrayIndexOutOfBoundsException => None
            }
        }
    }

    def isValidate : Boolean =
        if (pos == -1 || mark == -1 || limit == -1) false
        else if (mark > pos) false
        else if (limit > pageSize) false
        else if (limit <= pos) false
        else true

    def pageDate : Stream[String] = {
        def pageDateAcc() : Stream[String] = nextLine match {
            case None => Stream.empty
            case Some(str) =>
                str #:: pageDateAcc()
        }

        pageDateAcc()
    }

    def allDate : Stream[String] = {
        def pageAcc(c : Int) : Stream[String] = {
            if (c < fs.pageCount) {
                cur = c
                curInStorage
                pageDate #::: pageAcc(c + 1)
            } else Stream.empty
        }
        pageAcc(0)
    }

    val pageCount = fs.pageCount
}
