//package com.pharbers.common.spark
//
//import com.pharbers.panel.format.input.writable.PhExcelWritable
//import com.pharbers.sparkSteam.paction.actionbase.{NULLArgs, RDDArgs}
//import com.pharbers.sparkSteam.panelgen.NhwaYMActions
//import org.scalatest.FunSuite
//
//import scala.reflect.ClassTag
//
//class com.pharbers.max.SPFileFormatSuit extends FunSuite {
//    test("Spark File Convert") {
//        type T = ClassTag.type
//        case class a() extends NhwaYMActions
//        implicit def progressFunc(progress : Double, flag : String) : Unit = Unit
//        val b = a().perform(NULLArgs)
//        val c = b.asInstanceOf[RDDArgs[ClassTag.type]]
//        val d = c.get
//        println(d.count())
//    }
//}
