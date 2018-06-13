package com.pharbers.unitTest.common

import scala.io.Source
import scala.util.parsing.json.JSON

trait readJsonTrait {
    private val config_path: String = "pharbers_config/market_table.json"
    private def loadData(path: String): List[Map[String, String]] =
        JSON.parseFull(Source.fromFile(path).mkString)
                .get.asInstanceOf[List[Map[String, String]]]

    val testData: List[Map[String, String]] = loadData(config_path)
}
