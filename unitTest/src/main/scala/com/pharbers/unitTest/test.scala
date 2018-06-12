package com.pharbers.unitTest

import java.io.{File, FileInputStream}

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions.expr
import org.apache.spark.sql.DataFrame
import scala.io.Source


object test extends App {
    //    lazy val sparkDriver: phSparkDriver = phSparkDriver()
    //    val sqlContext = new SQLContext(sparkDriver.sc)
    //    val result = sqlContext.read.format("com.databricks.spark.csv")
    //            .option("header", "true") //这里如果在csv第一行有属性的话，没有就是"false"
    //            .option("inferSchema", true.toString) //这是自动推断属性列的数据类型。
    //            .option("delimiter", 31.toChar.toString)
    //            .load(s"/mnt/result/test001") //文件的路径
    //    result.show(200)
    
    //    val diffResult = phSparkDriver().csv2RDD("/mnt/result/test", ",")
    //    diffResult.show(300)
    
    //    val delimiter = 31.toChar.toString
    //    val maxResult = "a47a4fa5-d144-425e-baab-75a813b4d3f60c65ed6c-850a-4c01-b4bb-c7e2cf403cb3"
    //    val maxDF = phSparkDriver().csv2RDD(max_path_obj.p_maxPath + maxResult, delimiter)
    
    //    maxDF.select("Product", "f_sales").groupBy("Product").agg(expr("sum(f_sales)")).coalesce(1).write
    //            .format("csv")
    //            .option("header", value = true)
    //            .option("dilimiter", 31.toChar.toString)
    //            .save("/mnt/result/product")
    //    maxDF.select("Product").distinct().show(47)
    //    val a = maxDF.select("Product").distinct().count()
    //    maxDF.filter("Panel_ID == 'PHA0024696'").groupBy("Panel_ID").agg(expr("sum(f_sales)"), expr("sum(f_units)")).show()
    //    maxDF.filter("Panel_ID == 'PHA0024694'").show()
    
    //    maxDF.show()
    
    //    val xls_file: String = "/home/cui/download/zero.xlsx"
    //    val ins = new File(xls_file)
    //    println(ins.length())

    //纵向连接单元测试结果
//    val lst = List("5a9e0efa-1171-4239-99cc-91c2697c723e",
//        "c82b4a45-7a50-4fc8-a1da-660a3e7ac1fb",
//        "9dd7cff6-8555-4746-a27d-c33c4f1f2b15",
//        "4ef3cd21-222f-4ecc-a6d6-f85aae7626c6",
//        "633b8f3e-aacb-4315-8d53-24b972bb95a5",
//        "a2bc663f-bdbb-44f1-a3b1-644be7b113ed",
//        "9a4ee4e4-bef6-4b5c-8387-7ff46fd72f3a",
//        "f8428acc-e73f-4742-b3ca-680ef0a8baa2",
//        "7981faf7-00f5-474e-853a-23ea8ebe334f",
//        "832b534a-749c-417f-893e-c352e67ea608",
//        "89097ea1-c645-4e0b-a8b0-a5b520087b1d",
//        "0fb3e990-bf73-4444-98e1-0f68d71ea57b",
//        "22bb6c4e-eba7-4a8a-ac12-55d3ca22a912",
//        "8ac07d6a-e6a8-4a01-8b42-d811d9287e72",
//        "b5d7eef6-132c-4b1a-90fc-6df84bb94195",
//        "a2e17d1c-e267-4c53-84ae-f68cb2fa7a38",
//        "9cadde77-961f-4e4b-83bd-df948a455ad6")
//    val sparkDriver = phSparkDriver()
//    lst.map { f =>
//        sparkDriver.sqc.read.format("com.databricks.spark.csv")
//                .option("header", "true") //这里如果在csv第一行有属性的话，没有就是"false"
//                .option("inferSchema", true.toString) //这是自动推断属性列的数据类型。
//                .option("delimiter", 31.toChar.toString)
//                .load("/mnt/config/result/" + f)
//    } //文件的路径
//            .reduce((totalResult, f) => totalResult union f).coalesce(1).write
//            .format("csv")
//            .option("header", value = true)
//            .option("delimiter", 31.toChar.toString)
//            .save("/mnt/config/result/result")
    
    val sparkDriver = phSparkDriver()
    val lst = List(("zs", "1"), ("ls", "2"))
    lst.toDF("name", "age")
    sparkDriver.sqc.read.format("com.databricks.spark.csv")
            .option("header", "true") //这里如果在csv第一行有属性的话，没有就是"false"
            .option("inferSchema", true.toString) //这是自动推断属性列的数据类型。
            .option("delimiter", 31.toChar.toString)
            .load("/mnt/config/result/testread")
    
}
