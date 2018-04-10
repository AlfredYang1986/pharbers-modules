package com.pharbers.max

import java.util.Date
import org.scalatest.FunSuite
import java.text.SimpleDateFormat
import com.pharbers.calc.phMaxJob

/**
  * Created by jeorch on 18-4-10.
  */
class NewCalcSuite extends FunSuite{
    implicit def progressFunc(progress: Double, flag: String): Unit = Unit

    test("nhwa calc test") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"MAX计算开始时间" + dateformat.format(new Date()))
        println()

        val result = phMaxJob("8543ae17-5be8-4d40-ba83-ebffc5b2c21a.csv").perform().get

        println("result = " + result)
        println()
        println(s"MAX计算结束时间" + dateformat.format(new Date()))
    }
}
