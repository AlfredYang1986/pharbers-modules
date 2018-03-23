//package com.pharbers.common.spark
//
//import com.pharbers.sparkSteam.paction.actionbase.NULLArgs
//import com.pharbers.sparkSteam.panelgen.actionContainer.commonPanelActions
//import org.apache.spark.rdd.RDD
//import org.scalatest.FunSuite
//
//class SPFileFormatSuit extends FunSuite {
//    test("Spark File Convert") {
//        object a extends commonPanelActions
//        implicit def progressFunc(progress : Double, flag : String) : Unit = Unit
//        val b = a.perform(NULLArgs)
//        val c = b.get.asInstanceOf[RDD[Map[String, Any]]]
//        val d = c.count()
//        d
//    }
//}
