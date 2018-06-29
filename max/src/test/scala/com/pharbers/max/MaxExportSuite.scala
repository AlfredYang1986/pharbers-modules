package com.pharbers.max

import com.pharbers.pactions.actionbase.{MapArgs, StringArgs}
import com.pharbers.search.{phDeliverySearchDataJob, phExportSearchDataJob}
import org.scalatest.FunSuite

/**
  * Created by jeorch on 18-6-5.
  */
class MaxExportSuite extends FunSuite {

    val company: String = "5b028f95ed925c2c705b85ba"
    val user: String = "5b028feced925c2c705b85bb"
    val jobId: String = "20180623test001"
    val ym = "201804"
    val mkt = "INF"

    test("export search result"){

        val args: Map[String, String] = Map(
            "company" -> company,
            "ym_condition" -> "201804-201804",
            "mkt" -> mkt
        )

        val exportResult =  phExportSearchDataJob(args).perform().asInstanceOf[MapArgs].get("export_search_data_action").asInstanceOf[StringArgs].get
        println(exportResult)
    }

    test("export delivery result"){

        val args: Map[String, String] = Map(
            "company" -> company,
            "ym_condition" -> "201804-201804",
            "mkt" -> mkt
        )

        val exportResult =  phDeliverySearchDataJob(args).perform().asInstanceOf[MapArgs].get("export_delivery_data_action").asInstanceOf[MapArgs].get("delivery_data_action").asInstanceOf[StringArgs].get
        println(exportResult)
    }

}
