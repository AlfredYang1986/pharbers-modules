package com.pharbers.resultCheck

import java.util.Date

import org.scalatest.FunSuite
import java.text.SimpleDateFormat

import com.pharbers.spark.phSparkDriver
import com.pharbers.unitTest.startTest

class resultCheckSuit extends FunSuite {

    test("nhwa result check") {
        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
        println(s"开始检查时间" + dateformat.format(new Date()))
        println()


        new startTest().doTest()


        println()
        println(s"结束检查时间" + dateformat.format(new Date()))
    }

    test("adsfa") {
        import org.apache.spark.sql.functions.udf
        // 新建一个dataFrame
        val spark = phSparkDriver().ss
        val tempDataFrame = spark.createDataFrame(
            Seq(
                ("a", "asf"),
                ("b", "2143"),
                ("c", "rfds")
            )).toDF("id", "content")

        // 自定义udf的函数
        val code = (arg: String) => {
            if (arg.getClass.getName == "java.lang.String") 1 else 0
        }

        val addCol = udf(code)

        // 增加一列
        val addColDataframe = tempDataFrame.withColumn("col", addCol(tempDataFrame("id")))
        addColDataframe.show(10, false)
    }

}
