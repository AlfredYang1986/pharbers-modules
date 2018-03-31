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
//    val coll1 = "235f39e3da85c2dee2a2b20d004a8b77_Allelock_1712"
//    val mkt1 = "阿洛刻市场"
//    val coll_with_mkt1 = s"${coll1}##${mkt1}"
//    val destPath = s"/mnt/config/FileBase/${company_name}/Delivery"
//
//    val args: Map[String, List[String]] = Map(
//        "company" -> List(company_name),
//        "dbName" -> List(dbName),
//        "lstColl" -> List(coll_with_mkt1),
//        "destPath" -> List(destPath)
//    )
//    test("test astellas delivery"){
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
