//package com.pharbers.delivery.nhwa
//
//import com.pharbers.builder.SearchFacade
//import org.scalatest.FunSuite
//import play.api.libs.json.Json.toJson
//
///**
//  * Created by jeorch on 18-3-28.
//  */
//class NhwaDeliverySuite extends FunSuite {
//
//    val company: String = "5afa53bded925c05c6f69c54"
//    val user: String = "5afa57a1ed925c05c6f69c68"
//    val jobId: String = "20180523test001"
//    val mkt = "麻醉市场"
//
//    test("test nhwa delivery"){
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
//    }
//}
