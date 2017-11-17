package pfizer

import com.pharbers.memory.pages.fop.dir.dirPageStorage
import org.scalatest.FunSuite
import com.pharbers.memory.pages.{dirFlushMemory, pageMemory, pageMemory2, pageMemory3}

/**
  * Created by clock on 17-9-7.
  */
class PfizerSuite extends FunSuite {

//    test("page append memory") {
//        val t = flushMemory("files/test_flush")
//
//        t.appendLine("我是一个兵")
//        t.appendLine("来自老百姓")
//
//        t.flush
//        t.close
//    }
//
//    test("page append memory another") {
//        val t = flushMemory("files/test_flush")
//
//        t.appendLine("我是一个兵")
//        t.appendLine("来自老百姓")
//
//        t.flush
//        t.close
//    }

//    test("page memory") {
////        val t = pageMemory2("files/test")
//        val t = pageMemory2("files/data2")
//        println(t.allData.length)
//
//        t.allData.zipWithIndex.foreach { x =>
//            println(s"${x._2} : ${x._1}")
//        }
//
////            t.pageData(1).zipWithIndex.foreach { x =>
////                println(s"${x._2} : ${x._1}")
////            }
//
//        t.pageCount
//    }

//    test ("dri opt write") {
//        val d = dirFlushMemory("files/dir")
//
//        val t = pageMemory3("files/data")
//
////        var page = t.pageCount.toInt
//        var i = 0
//        t.allData { s => s.foreach { iter =>
//            d.appendLine(iter.toString)
//            i = i + 1
//        }}
//        d.close
//        println(s"i is $i")
//
//        i = 0
//
//        val dr = dirPageStorage("files/dir")
//        dr.readAllData(_ => i = i + 1)
//        println(s"i2 is $i")
//    }

    // test ("dirPageStorage") {
    //     var i = 0

    //     val dr = dirPageStorage("files/dir")
    //     dr.readAllData(_ => i = i + 1)
    //     println(s"page is $i")
    // }
}
