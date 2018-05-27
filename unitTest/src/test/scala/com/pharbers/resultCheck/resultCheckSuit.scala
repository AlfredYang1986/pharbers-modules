package com.pharbers.resultCheck

import java.util.Date
import org.scalatest.FunSuite
import java.text.SimpleDateFormat
import com.pharbers.unitTest.startTest

class resultCheckSuit extends FunSuite{

    test("nhwa result check") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"开始检查时间" + dateformat.format(new Date()))
        println()


        new startTest().doTest()


        println()
        println(s"结束检查时间" + dateformat.format(new Date()))
    }

}
