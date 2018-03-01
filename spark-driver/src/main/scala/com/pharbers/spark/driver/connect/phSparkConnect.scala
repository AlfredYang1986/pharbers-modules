package com.pharbers.spark.driver.connect

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by clock on 18-2-26.
  */
trait phSparkConnect extends phSparkConfig{
    private val conf = new SparkConf()
            .setAppName(sparkMasterName)
            .setMaster(s"spark://$sparkMasterHost:$sparkMasterPort")
///需要以下两个jar包，目前在spark服务端配置
//            .setJars("/home/clock/.m2/repository/org/mongodb/spark/mongo-spark-connector_2.10/0.1/mongo-spark-connector_2.10-0.1.jar" ::
//                    "/home/clock/.m2/repository/org/mongodb/mongo-java-driver/3.2.2/mongo-java-driver-3.2.2.jar" ::  Nil)

    val sc = new SparkContext(conf)
}