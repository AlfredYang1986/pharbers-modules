package pfizer

import org.scalatest.FunSuite
import com.pharbers.memory.pages.page2map

/**
  * Created by clock on 17-9-7.
  */
class PfizerSuite extends FunSuite {

    test("page memory") {
        val t = page2map("files/test.cache")
        println(s"t.size = ${t.allLength}")
        println(s"t.pageCount = ${t.pageCount}")

//        t.allData.zipWithIndex.foreach { x =>
//            println(s"${x._2} : ${x._1}")
//        }

        t.pageData(8).zipWithIndex.foreach { x =>
            println(s"${x._2} : ${x._1}")
        }
    }
}
