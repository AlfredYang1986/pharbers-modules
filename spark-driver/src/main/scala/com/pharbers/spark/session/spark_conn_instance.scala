package com.pharbers.spark.session

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by clock on 18-2-26.
  */
trait spark_conn_instance extends spark_conn_config {
    private val conf = new SparkConf()
            .setAppName(sparkMasterName)
            .setMaster(s"spark://$sparkMasterHost:$sparkMasterPort")
            .set("spark.executor.memory", "2g")
//            .set("spark.executor.cores", "2")
            .set("spark.executor.extraJavaOptions",
                """
                  | -XX:+UseG1GC -XX:+PrintFlagsFinal
                  | -XX:+PrintReferenceGC -verbose:gc
                  | -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
                  | -XX:+PrintAdaptiveSizePolicy -XX:+UnlockDiagnosticVMOptions
                  | -XX:+G1SummarizeConcMark
                  | -XX:InitiatingHeapOccupancyPercent=35 -XX:ConcGCThreads=1
                """.stripMargin)
/////需要以下两个jar包，目前在spark服务端配置
////            .setJars("/home/clock/.m2/repository/org/mongodb/spark/mongo-spark-connector_2.10/0.1/mongo-spark-connector_2.10-0.1.jar" ::
////                    "/home/clock/.m2/repository/org/mongodb/mongo-java-driver/3.2.2/mongo-java-driver-3.2.2.jar" ::  Nil)


    val spark_session: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    val spark_context: SparkContext = spark_session.sparkContext
    val spark_sql_context: SQLContext = spark_session.sqlContext
}
