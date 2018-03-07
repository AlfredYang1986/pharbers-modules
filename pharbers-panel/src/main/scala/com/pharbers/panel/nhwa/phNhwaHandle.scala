package com.pharbers.panel.nhwa

import scala.collection.immutable.Map
import com.pharbers.spark.driver.phSparkDriver

/**
  * Created by clock on 18-3-7.
  */
case class phNhwaHandle(args: Map[String, List[String]]) extends phNhwaCalcYm with phNhwaPanel {
    override val cpa_location: String = "hdfs://192.168.100.174:12138/user/jeorch/180211恩华17年1-12月检索.csv"
    override val sparkDriver: phSparkDriver = phSparkDriver()
}