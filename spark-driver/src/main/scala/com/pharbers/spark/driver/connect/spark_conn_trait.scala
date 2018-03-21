package com.pharbers.spark.driver.connect

/**
  * Created by clock on 18-2-27.
  */
trait spark_conn_trait {
    implicit val conn_instance = new spark_conn_obj
}
