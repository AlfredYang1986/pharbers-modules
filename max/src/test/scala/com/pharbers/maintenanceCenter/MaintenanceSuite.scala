package com.pharbers.maintenanceCenter

import com.pharbers.builder.MaintenanceCenter
import org.scalatest.FunSuite
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-6-6.
  */
class MaintenanceSuite extends FunSuite {

    val maintenance = new MaintenanceCenter

    val company = "5b023787810c6e0268fe6ff6"
    val condition = toJson {
        Map(
            "condition" -> toJson(Map("maintenance" -> toJson(Map("company_id" -> toJson(company)))))
        )
    }

    test("Maintenance Center get all companies"){
        maintenance.getAllCompanies._1.get.get("companies").get.as[List[Map[String, String]]].foreach(println)
    }

    test("Maintenance Center get all dataCleanModuleArgs"){
        println(maintenance.getDataCleanModuleArgs(condition)._1.get)
    }

    test("Maintenance Center get all simpleModuleArgs"){
        println(maintenance.getSimpleModuleArgs(condition)._1.get)
    }

    test("Maintenance Center get all maxModuleArgs"){
        println(maintenance.getMaxModuleArgs(condition)._1.get)
    }

    test("Maintenance Center get all deliveryModuleArgs"){
        println(maintenance.getDeliveryModuleArgs(condition)._1.get)
    }

    test("Maintenance Center replaceMatchFile"){
        val replaceCondition = toJson {
            Map(
                "condition" -> toJson(
                    Map(
                        "maintenance" -> toJson(Map("company_id" -> toJson(company))),
                        "origin_file" -> toJson(
                            Map("file_key" -> toJson("product_match_file"),
                                "file_name" -> toJson("20171018药品最小单位IMS packid匹配表.xlsx"))),
                        "current_file" -> toJson(
                            Map("file_uuid" -> toJson("testUUID.xlsx"),
                                "file_key" -> toJson("current_file_key"),
                                "file_name" -> toJson("current_file_name")))
                    )
                )
            )
        }
        maintenance.replaceMatchFile(replaceCondition)
    }

}
