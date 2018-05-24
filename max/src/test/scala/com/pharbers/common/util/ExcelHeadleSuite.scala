//package com.pharbers.common.util
//
//import com.pharbers.common.excel.{phExcelFileInfo, phHandleExcel}
//import org.scalatest.FunSuite
//
///**
//  * Created by clock on 17-9-7.
//  */
//class ExcelHeadleSuite extends FunSuite{
//    val file_local = "/Users/clock/Library/Containers/com.tencent.xinWeChat/Data/Library/Application Support/com.tencent.xinWeChat/2.0b4.0.9/668745fe0a6c5cd2fd05b528ef99b991/Message/MessageTemp/ebb6cf4415a068bc5b4b6a8410f404ac/File/pfz_2017年基准线.xlsx"
//
////    test("test get sheet count") {
////        val file_local = file_base + markets_file
////        val count = phHandleExcel().getCount(phExcelFileInfo(file_local))
////        println(s"sheet number = $count")
////    }
////
////    test("test get sheet name") {
////        val file_local = file_base + markets_file
////        val nameLst = phHandleExcel().getSheetNames(phExcelFileInfo(file_local))
////        nameLst.foreach(println)
////    }
////
//    test("read excel => simple") {
//        val list = phHandleExcel().read2Lst(phExcelFileInfo(file_local))
//        list.foreach(println)
//    }
////
////    test("read excel => specified sheet and filter") {
////        //导入指定页数,进行filter,不设置字段名,没有默认值
////        val file_local = file_base + markets_file
////        implicit val filterFun: Map[String,String] => Boolean = { tr =>
////            tr.get("Market") match {
////                case None => false
////                case Some(s) if s.startsWith("A") => true
////                case _ => false
////            }
////        }
////        val totle = phHandleExcel().getCount(phExcelFileInfo(file_local))
////        for (i <- 1 to totle)
////            phHandleExcel().read2Lst(phExcelFileInfo(file_local, i)).foreach(println)
////    }
////
////    test("read excel => setField and setDefault") {
////        val file_local = file_base + markets_file
////        val setFieldMap = Map("Market" -> "TEST" )
////        val setDefaultMap = Map("通用名_原始" -> "0", "TEST" -> "$通用名_原始" )
////
////        phHandleExcel().read2Lst(phExcelFileInfo(file_local, 1, setFieldMap, setDefaultMap)).foreach(println)
////    }
////
////    test("read excel => post function") {
////        val file_local = file_base + markets_file
////        //新建列
////        implicit val postFun: Map[String,String] => Option[Map[String, String]] = { tr =>
////            Some(tr ++ Map("DOIE" -> tr("Market")))
////        }
////
////        phHandleExcel().read2Lst(phExcelFileInfo(file_local)).foreach(println)
////    }
//
//}
