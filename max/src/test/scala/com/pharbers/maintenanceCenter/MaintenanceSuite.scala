package com.pharbers.maintenanceCenter

import com.pharbers.builder.MaintenanceCenter
import org.scalatest.FunSuite
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-6-6.
  */
class MaintenanceSuite extends FunSuite {

    val maintenance = new MaintenanceCenter

    val company: String = "5b023787810c6e0268fe6ff6"

    def searchCondition(module_tag: String) = toJson {
        Map(
            "condition" -> toJson(Map("maintenance" -> toJson(
                Map("company_id" -> toJson(company),
                    "module_tag" -> toJson(module_tag)))))
        )
    }

    val replaceCondition = toJson {
        Map(
            "condition" -> toJson(
                Map(
                    "maintenance" -> toJson(
                        Map("company_id" -> toJson(company),
                            "module_tag" -> toJson("clean"))),
                    "origin_file" -> toJson(
                        Map("file_key" -> toJson("product_match_file"))),
                    "current_file" -> toJson(
                        Map("file_uuid" -> toJson("nhwa匹配表re.xlsx")))
                )
            )
        )
    }

    test("Maintenance Center get all companies"){
        maintenance.getAllCompaniesLst._1.get.get("companies").get.as[List[Map[String, String]]].foreach(println)
    }

    test("Maintenance Center get all dataCleanModuleArgs"){
        println(maintenance.getSingleModuleArgs(searchCondition("clean"))._1.get)
    }

    test("Maintenance Center get all simpleModuleArgs"){
        println(maintenance.getSingleModuleArgs(searchCondition("panel"))._1.get)
    }

    test("Maintenance Center get all maxModuleArgs"){
        println(maintenance.getSingleModuleArgs(searchCondition("max"))._1.get)
    }

    test("Maintenance Center get all deliveryModuleArgs"){
        println(maintenance.getSingleModuleArgs(searchCondition("delivery"))._1.get)
    }

    test("Maintenance Center replaceMatchFile"){
        maintenance.replaceMatchFile(replaceCondition)
    }

}

