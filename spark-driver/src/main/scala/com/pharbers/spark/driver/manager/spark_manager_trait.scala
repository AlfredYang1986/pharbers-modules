package com.pharbers.spark.driver.manager

import org.bson.Document
import com.mongodb.spark.MongoSpark
import com.mongodb.spark.rdd.MongoRDD
import com.mongodb.spark.config.ReadConfig
import com.pharbers.spark.driver.connect.spark_conn_instance

/**
  * Created by clock on 18-2-27.
  */

trait spark_managers extends mongo2RDD with csv2RDD

sealed trait spark_manager_trait {
    implicit val conn_instance: spark_conn_instance
    val ss = conn_instance.spark_session
    val sc = conn_instance.spark_context
    val sqc = conn_instance.spark_sql_context
}

trait mongo2RDD extends spark_manager_trait {
    def mongo2RDD(mongodbHost: String,
                  mongodbPort: String,
                  databaseName: String,
                  collName: String,
                  readPreferenceName: String = "secondaryPreferred"): MongoRDD[Document] = {
        val readConfig = ReadConfig(Map(
            "spark.mongodb.input.uri" -> s"mongodb://$mongodbHost:$mongodbPort/",
            "spark.mongodb.input.database" -> databaseName,
            "spark.mongodb.input.collection" -> collName,
            "readPreference.name" -> readPreferenceName)
        )
        MongoSpark.load(sc, readConfig = readConfig)
    }
}

trait csv2RDD extends spark_manager_trait {
    def csv2RDD(file_path: String,
                delimiter: String = ",", header: Boolean = true) = {
        ss.read.format("csv")
                .option("header", header)
                .option("mode", "DROPMALFORMED")
                .option("delimiter", delimiter)
                .csv(file_path)
    }
}