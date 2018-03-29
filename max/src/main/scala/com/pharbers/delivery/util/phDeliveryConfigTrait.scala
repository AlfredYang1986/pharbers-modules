package com.pharbers.delivery.util

import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by jeorch on 18-3-29.
  */
trait phDeliveryConfigTrait extends PharbersInjectModule {
    val company: String
    override val id: String = s"company_${company}"
    override val configPath: String = "pharbers_config/delivery-config.xml"
    override val md =
        "hospital_match_file" :: "medicine_match_file" :: Nil
}

case class phDeliveryConfig(company: String) extends phDeliveryConfigTrait {
    lazy val hospitalMatchFile: String = config.mc.find(p => p._1 == "hospital_match_file").get._2.toString
    lazy val medicineMatchFile: String = config.mc.find(p => p._1 == "medicine_match_file").get._2.toString
}
