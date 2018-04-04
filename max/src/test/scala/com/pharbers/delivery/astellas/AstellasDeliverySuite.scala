//package com.pharbers.panel.nhwa
//
//import java.io.File
//
//import com.pharbers.panel.astellas.phAstellasHandle
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-3-28.
//  */
//class AstellasDeliverySuite extends FunSuite {
//    val company_name = "235f39e3da85c2dee2a2b20d004a8b77"
//    val dbName = "Max_Test"
//    val yearMonth = "1712"
//    val destPath = s"/mnt/config/FileBase/${company_name}/HistoryDeliveryFiles/Max_14-${yearMonth}"
//
//    test("test astellas delivery"){
//
//        val testPath = new File("/home/jeorch/work/test/bash-test")
//        val lst_coll_with_mkt = testPath.listFiles().map { one =>
//            val tempFile = one.getName
//            val tempColl = tempFile.split(".csv").head
//            val tempMkt = tempFile.split("_").head match {
//                case "Allelock" =>"阿洛刻市场"
//                case "Mycamine" => "米开民市场"
//                case "Prograf" => "普乐可复市场"
//                case "Perdipine" => "佩尔市场"
//                case "Harnal" => "哈乐市场"
//                case "Gout" => "痛风市场"
//                case "Vesicare" => "卫喜康市场"
//                case "Grafalon" => "GRAFALON市场"
//            }
//            s"${tempColl}##${tempMkt}"
//        }.toList
//        val args: Map[String, List[String]] = Map(
//            "company" -> List(company_name),
//            "dbName" -> List(dbName),
//            "lstColl" -> lst_coll_with_mkt,
//            "destPath" -> List(destPath)
//        )
////        lst_coll_with_mkt.foreach(println)
//        delTempFile(new File(destPath))
//        phAstellasHandle(args).generateDeliveryFile
//    }
//
//    def delTempFile(fileName: File): Unit ={
//        if(fileName.isDirectory) {
//            fileName.listFiles().toList match {
//                case Nil => fileName.delete()
//                case lstFile => lstFile.foreach(delTempFile); fileName.delete()
//            }
//        } else {
//            fileName.delete()
//        }
//    }
//}
