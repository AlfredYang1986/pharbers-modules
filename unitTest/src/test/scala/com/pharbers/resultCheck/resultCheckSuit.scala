package com.pharbers.resultCheck

import java.util.Date
import org.scalatest.FunSuite
import java.text.SimpleDateFormat
import com.pharbers.pactions.actionbase.StringArgs
import com.pharbers.unitTest.startTest

class resultCheckSuit extends FunSuite {

    test("nhwa result check") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"开始检查时间" + dateformat.format(new Date()))
        println()
        
//        val lst = startTest().doTest()
//        println(lst)
        val totalResult = startTest().writeTotalResult()
        println(totalResult.asInstanceOf[StringArgs].get)
        
//        val a = phSparkDriver().csv2RDD("/mnt/config/Cache/ee94268a-858b-4adc-8b97-3c96f35ebabb/universe_file/part-00000")
//        a.show(false)
//        println(a.count())
        
        println()
        println(s"结束检查时间" + dateformat.format(new Date()))
    }
}
