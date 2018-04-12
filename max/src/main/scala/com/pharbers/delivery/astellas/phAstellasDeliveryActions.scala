package com.pharbers.delivery.astellas

import com.pharbers.delivery.util.phDeliveryConfig

/**
  * Created by jeorch on 18-3-28.
  */
case class phAstellasDeliveryActions(args: Map[String, List[String]]) extends phAstellasDeliveryActionsTrait {
    override val name = ""
    lazy val deliveryConfig = phDeliveryConfig(company)
    override lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
    override lazy val dbName: String = args.getOrElse("dbName", throw new Exception("no find dbName arg")).head
    override lazy val lstColl: List[String] = args.getOrElse("lstColl", throw new Exception("no find lstColl arg"))
    override lazy val destPath: String = args.getOrElse("destPath", throw new Exception("no find destPath arg")).head
    override lazy val hospitalMatchFile: String = deliveryConfig.hospitalMatchFile
    override lazy val medicineMatchFile: String = deliveryConfig.medicineMatchFile
    override lazy val historyFile: String = deliveryConfig.historyFile
}
