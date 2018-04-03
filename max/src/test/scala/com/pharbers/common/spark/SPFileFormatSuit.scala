//package com.pharbers.common.spark
//
//import com.pharbers.pactions.actionbase.NULLArgs
//import com.pharbers.pactions.generalactions.xlsxReadingAction
//import com.pharbers.panel.astellas.format.phAstellasCpaFormat
//import com.pharbers.spark.phSparkDriver
//import org.apache.spark.rdd.RDD
//import org.scalatest.FunSuite
//
//class SPFileFormatSuit extends FunSuite {
//    test("Spark File Convert") {
//        val cpa = xlsxReadingAction[phAstellasCpaFormat]("resource/8ee0ca24796f9b7f284d931650edbd4b/Client/171215恩华2017年10月检索.xlsx", "cpa").perform(NULLArgs)
//        val cpaRDD = cpa.get.asInstanceOf[RDD]
//        val pd = phSparkDriver().ss
//        import pd.implicits._
//        val b = pd.createDataFrame(cpaRDD)
//        println(cpaRDD.count())
//    }
//}
