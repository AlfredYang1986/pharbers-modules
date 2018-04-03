package com.pharbers.spark

import com.pharbers.spark.session.spark_conn_trait

/**
  * Created by clock on 18-2-26.
  */
case class phSparkDriver() extends spark_conn_trait with spark_managers
