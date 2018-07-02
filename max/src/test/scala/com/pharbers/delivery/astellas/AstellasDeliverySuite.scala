//package com.pharbers.panel.nhwa
//
//import java.io.File
//
//import com.pharbers.builder.SearchFacade
//import com.pharbers.delivery.astellas.phAstellasDeliveryActions
//import org.scalatest.FunSuite
//import play.api.libs.json.Json.toJson
//
///**
//  * Created by jeorch on 18-3-28.
//  */
//class AstellasDeliverySuite extends FunSuite {
//
//    test("test astellas delivery"){
//
//        val company: String = "5b023787810c6e0268fe6ff6"
//        val mkt = "阿洛刻市场"
//
//        val condition = toJson {
//            Map(
//                "condition" -> toJson(Map(
//                    "startTime" -> toJson("201701"),
//                    "endTime" -> toJson("201801"),
//                    "market" -> toJson(mkt)
//                )),
//                "user" -> toJson(Map("company" -> toJson(Map("company_id" -> toJson(company)))))
//            )
//        }
//
//        val search = new SearchFacade
//        println(search.exportDelivery(condition)._1.get.get("delivery_file_name").get)
//
//    }
//
//}
