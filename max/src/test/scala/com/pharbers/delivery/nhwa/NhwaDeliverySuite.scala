//package com.pharbers.delivery.nhwa
//
//import com.pharbers.panel.nhwa.phNhwaHandle
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-3-28.
//  */
//class NhwaDeliverySuite extends FunSuite {
//    val company_name = "8ee0ca24796f9b7f284d931650edbd4b"
//    val dbName = "Max_Cores"
//    val coll1 = "8ee0ca24796f9b7f284d931650edbd4ba2c5e077-4231-4e74-9c11-8f17391c0ab0"
//    val destPath = "/home/jeorch/桌面"
//
//    val args: Map[String, List[String]] = Map(
//        "company" -> List(company_name),
//        "dbName" -> List(dbName),
//        "lstColl" -> List(coll1),
//        "destPath" -> List(destPath)
//    )
//
//    test("test nhwa delivery"){
//        phNhwaHandle(args).generateDeliveryFile
//    }
//}
