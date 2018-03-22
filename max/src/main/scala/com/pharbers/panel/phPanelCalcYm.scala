package com.pharbers.panel

import com.pharbers.spark.driver.phSparkDriver
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 18-3-7.
  */
trait phPanelCalcYm extends phPanelTrait {
    val sparkDriver: phSparkDriver
    val cpa_location: String
    val gycx_location: String

    override def calcYM: JsValue = {
        val rdd = sparkDriver.csv2RDD(cpa_location, delimiter = 31.toChar.toString, header = true)
        val temp = rdd.groupBy("YM", "HOSPITAL_CODE").count()
                .groupBy("YM").count()
                .collect()
                .map{row => (row.getString(0), row.getLong(1))}
        val max = temp.map(_._2).max
        val result = temp.filter(_._2 > max/2).map(_._1).sorted
        sparkDriver.ss.stop()
        toJson(result.mkString(comma))
    }
}
