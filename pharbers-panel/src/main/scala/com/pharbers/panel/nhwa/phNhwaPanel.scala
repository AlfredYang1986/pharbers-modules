package com.pharbers.panel.nhwa

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.panel.phPanelTrait
import com.pharbers.spark.driver.phSparkDriver

/**
  * Created by clock on 18-3-6.
  */
trait phNhwaPanel extends phPanelTrait  {
    val sparkDriver: phSparkDriver
    val cpa_location: String
    val m1_location = "hdfs://192.168.100.174:12138/user/jeorch/匹配表.csv"
    val hos_location = "hdfs://192.168.100.174:12138/user/jeorch/universe_麻醉市场_online.csv"
    val b0_location = "hdfs://192.168.100.174:12138/user/jeorch/通用名市场定义.csv"

    override def getPanelFile(ym: List[String], mkt: String, t: Int = 0, c: Int = 0): JsValue = {
        val c0_rdd = load(cpa_location)
        val m1_rdd = load(m1_location).distinct()
        val hos00_rdd = load(hos_location)
        val b0_rdd = load(b0_location)
        val m1_c = m1_rdd.join(b0_rdd, b0_rdd("通用名_原始") <=> m1_rdd("通用名"))
        val hos0 = hos00_rdd.filter(s"DOI = '$mkt'").select("HOSP_ID").distinct()

        val maps = hos0.rdd.map(x => x.getInt(0))
        val c = maps.count()
        toJson("")
    }

    private def load(file_path: String) = sparkDriver.csv2RDD(file_path, delimiter = 31.toChar.toString)
}
