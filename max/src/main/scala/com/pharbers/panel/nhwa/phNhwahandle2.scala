package com.pharbers.panel.nhwa

import com.pharbers.spark.driver.phSparkDriver
import play.api.libs.json.{JsNull, JsValue}

class phNhwahandle2(override val sparkDriver: phSparkDriver) extends phNhwaPanel {
    override val m1_location: String = "resource/result/m1"
    override val hos_location: String = "resource/result/hosp"
    override val b0_location: String = "resource/result/b0"
    override val not_arrival_hosp_location: String = "resource/result/not_arrive"
    override val not_published_hosp_location: String = "resource/result/not_published_hosp_file"
    override val full_hosp_location: String = "resource/nhwa/补充医院.csv"
    override val cpa_location: String = "resource/result/cpa"
    override val gycx_location: String = ""
    override val output_location: String = "resource/panel/"
    override val company: String = "alfred"
    override val client_location: String = "alfred"

    override def calcYM: JsValue = JsNull
}
