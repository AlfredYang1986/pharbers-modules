package com.pharbers.pageMemory.fop.read

import java.nio.charset.StandardCharsets

trait pageStorage2 {

    protected val chl = '\n'.toByte
    protected val pageSize : Int
    protected val fs : fileStorageTrait

    protected var limit_p : Long = -1
    protected var mark_p = -1
    protected var pos_p = -1

    lazy val buf : Array[Byte] = new Array[Byte](pageSize)
    var cur_page : Int = 0

    def curInStorage : Array[Byte] = {
        limit_p = fs.capCurrentPage(cur_page, buf)
        mark_p = 0
        pos_p = 0

        buf
    }

    def nextLine : Option[String] = {
        def nextCharAcc(c : Int) : Array[Byte] = {
            if (buf(c) == chl) {
                val reVal = buf.slice(mark_p, c)
                mark_p = c + 1
                pos_p = c + 1
                reVal
            } else nextCharAcc(c + 1)
        }

        if (!isValidata) None
        else {
            try {
                val line = nextCharAcc(pos_p)
                Some(new String(line, StandardCharsets.UTF_8))
            } catch {
                case _ : java.lang.ArrayIndexOutOfBoundsException => None
            }
        }
    }

    def isValidata : Boolean =
        if (pos_p == -1 || mark_p == -1 || limit_p == -1) false
        else if (mark_p > pos_p) false
        else if (limit_p > pageSize) false
        else if (limit_p <= pos_p) false
        else true

    def pageData : Stream[String] = {
        def pageDateAcc() : Stream[String] = nextLine match {
            case None => Stream.empty
            case Some(str) =>
                str #:: pageDateAcc()
        }

        pageDateAcc()
    }

    def allData(func : Stream[String] => Unit) : Unit = {
        def pageAcc(c : Int) : Stream[String] = {
            if (fs.hasNextPage) {
                curInStorage
                func(pageData)
                pageAcc(c + 1)
            } else Stream.empty
        }
        pageAcc(0)
    }
}
