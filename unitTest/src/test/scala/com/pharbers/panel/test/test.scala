package com.pharbers.panel.test

import java.lang.Object
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.DataFrame

object test extends App {
//    val sparkDriver = phSparkDriver()
//    import sparkDriver.ss.implicits._
//    val lst = List(("aa","bb"), ("cc", "dd"))
//    val lstDF: DataFrame = lst.toDF("a", "b").as
//    lstDF.show()
    val temp = ("str1", "str2", "str3", "str4", "str5", "str6")
    println(temp.getClass)
}
