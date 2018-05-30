package com.pharbers.unitTest.action

import org.apache.spark.sql.DataFrame
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.functions.udf
import com.pharbers.pactions.actionbase._
import com.pharbers.common.algorithm.max_path_obj

object resultCheckAction {
    def apply(args: MapArgs): pActionTrait = new resultCheckAction(args)
}

class resultCheckAction(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "result_check"
    val delimiter: String = 31.toChar.toString

    override def perform(prMap : pActionArgs): pActionArgs = {

        val maxResult = "72636467-b544-4e4a-ac2f-e53efc3f74ff32250697-8638-4c3b-87a1-050d27253699"//prMap.asInstanceOf[MapArgs].get("max_result").asInstanceOf[StringArgs].get
        val maxDF = phSparkDriver().csv2RDD(max_path_obj.p_maxPath + maxResult, delimiter)
        val offlineDF = prMap.asInstanceOf[MapArgs].get("offline_result")
                .asInstanceOf[DFArgs].get
                .withColumnRenamed("类别", "scope")
                .withColumnRenamed("f_sales", "offlineSales")
                .withColumnRenamed("f_units", "offlineUnits")
                .withColumnRenamed("产品个数", "offlineProductNum")
                .withColumnRenamed("医院个数", "offlineHospNum")

        val sparkDriver = phSparkDriver()
        sparkDriver.sc.addJar("./target/pharbers-unitTest-0.1.jar")

        //比较全国
        val ckTotalDF: DataFrame = {
            val onlineSales = maxDF.agg(Map("f_sales" -> "sum")).collect().head.getDouble(0).toString
            val onlineUnits = maxDF.agg(Map("f_units" -> "sum")).collect().head.getDouble(0).toString
            val onlineProductNum = maxDF.select("Product").distinct().count().toString
            val onlineHospNum = maxDF.select("Panel_ID").distinct().count().toString

            val offlineTotalResult = offlineDF.filter(" scope == 'ALL' ")

            val onlineTotalResult = sparkDriver.ss
                    .createDataFrame(
                        Seq(
                            (onlineSales, onlineUnits, onlineProductNum, onlineHospNum)
                        )).toDF("onlineSales", "onlineUnits", "onlineProductNum", "onlineHospNum")

            val totalResult = offlineTotalResult join onlineTotalResult
            totalResult.withColumn("salesBoolean", addBooleanCol(totalResult("offlineSales"), totalResult("onlineSales")))
                    .withColumn("unitsBoolean", addBooleanCol(totalResult("offlineUnits"), totalResult("onlineUnits")))
                    .withColumn("productBoolean", addBooleanCol(totalResult("offlineProductNum"), totalResult("onlineProductNum")))
                    .withColumn("hospBoolean", addBooleanCol(totalResult("offlineHospNum"), totalResult("onlineHospNum")))
        }

//        //比较省份
//        val ckProvinceDF: DataFrame = {
//            val totalSales = maxDF.agg(Map("f_sales" -> "sum")).collect().head.getDouble(0)
//            val totalUnits = maxDF.agg(Map("f_units" -> "sum")).collect().head.getDouble(0)
//            val totalProductNum = maxDF.select("Product").distinct().count()
//            val totalHospNum = maxDF.select("Panel_ID").distinct().count()
//            val newDF = offlineDF.filter(" scope == 'ALL' ")
//                    .withColumn("onlineSales", addCol(offlineDF("scope")))
//            newDF
//        }
//
//        //比较城市
//        val ckCityDF: DataFrame = {
//            val totalSales = maxDF.agg(Map("f_sales" -> "sum")).collect().head.getDouble(0)
//            val totalUnits = maxDF.agg(Map("f_units" -> "sum")).collect().head.getDouble(0)
//            val totalProductNum = maxDF.select("Product").distinct().count()
//            val totalHospNum = maxDF.select("Panel_ID").distinct().count()
//            val newDF = offlineDF.filter(" scope == 'ALL' ")
//                    .withColumn("onlineSales", addCol(offlineDF("scope")))
//            newDF
//        }
//
//        //比较医院
//        val ckHospDF: DataFrame = {
//            val totalSales = maxDF.agg(Map("f_sales" -> "sum")).collect().head.getDouble(0)
//            val totalUnits = maxDF.agg(Map("f_units" -> "sum")).collect().head.getDouble(0)
//            val totalProductNum = maxDF.select("Product").distinct().count()
//            val totalHospNum = maxDF.select("Panel_ID").distinct().count()
//            val newDF = offlineDF.filter(" scope == 'ALL' ")
//                    .withColumn("onlineSales", addCol(offlineDF("scope")))
//            newDF
//        }



//        maxDF.show(false)
        ckTotalDF.show(false)
        NULLArgs
    }

    // 自定义udf的函数
    val ~= = (d1: String, d2: String) => {
        if((d1.toDouble - d2.toDouble).abs < 1.0E-3) true
        else false
    }

    val addBooleanCol = udf(~=)
}