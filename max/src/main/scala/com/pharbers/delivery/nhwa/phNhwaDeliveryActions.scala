package com.pharbers.delivery.nhwa

/**
  * Created by jeorch on 18-3-28.
  */
case class phNhwaDeliveryActions(args: Map[String, List[String]]) extends phNhwaDeliveryActionsTrait {
    override val name = "aa"
    lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
    lazy val dbName: String = args.getOrElse("dbName", throw new Exception("no find dbName arg")).head
    lazy val lstColl: List[String] = args.getOrElse("lstColl", throw new Exception("no find lstColl arg"))
    lazy val destPath: String = args.getOrElse("destPath", throw new Exception("no find destPath arg")).head
}
