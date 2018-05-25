package com.pharbers.unitTest.readFiles

import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.{DataFrame, SQLContext}

class LoadCSVFile {
    def getDataSet(csvFileName: String): DataFrame ={
        lazy val sparkDriver: phSparkDriver = phSparkDriver()
        val sqlContext = new SQLContext(sparkDriver.sc)
        sqlContext.read.format("com.databricks.spark.csv")
                .option("header", "true") //这里如果在csv第一行有属性的话，没有就是"false"
                .option("inferSchema", true.toString) //这是自动推断属性列的数据类型。
                .option("delimiter", 31.toChar.toString)
                .load(s"/mnt/config/Result/$csvFileName") //文件的路径
                .withColumnRenamed("f_sales", "sales")
                .withColumnRenamed("f_units", "units")
    }
    
}

object LoadCSVFile{
    def apply(): LoadCSVFile = new LoadCSVFile()
}
