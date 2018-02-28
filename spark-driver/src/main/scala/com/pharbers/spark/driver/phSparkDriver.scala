package com.pharbers.spark.driver

import com.pharbers.spark.driver.manager.spark_managers
import com.pharbers.spark.driver.connect.spark_conn_trait

/**
  * Created by clock on 18-2-26.
  */
case class phSparkDriver() extends spark_conn_trait with spark_managers
