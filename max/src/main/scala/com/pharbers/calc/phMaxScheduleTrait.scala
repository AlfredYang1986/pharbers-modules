package com.pharbers.calc

import java.io.File
import java.util.UUID

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.delivery.util.mongo_config_obj
import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver

trait phMaxScheduleTrait {

    private val maxSingleDayJobsKey = Sercurity.md5Hash("Pharbers")
    private val delimiter = 31.toChar.toString

    def rdd2mongo: Int = {
        val rd = new PhRedisDriver()
        val sd = phSparkDriver()
        var rdd2mongoJobsCount = 0
        rd.getSetAllValue(maxSingleDayJobsKey).foreach(singleJobKey => {
            val maxName = rd.getMapValue(singleJobKey, "max_result_name")
            val resultLocation = max_path_obj.p_maxPath + maxName
            val singleJobDF = sd.csv2RDD(resultLocation, delimiter)
            singleJobDF.write.format("com.mongodb.spark.sql.DefaultSource").mode("overwrite")
                .option("uri", s"mongodb://${mongo_config_obj.mongodbHost}:${mongo_config_obj.mongodbPort}/")
                .option("database", mongo_config_obj.databaseName)
                .option("collection", singleJobKey)
                .save()
            rdd2mongoJobsCount += 1
        })
        rd.flush
        rdd2mongoJobsCount
    }

    def mongo2rdd: Int = {
        val rd = new PhRedisDriver()
        val sd = phSparkDriver()
        var mongo2rddJobsCount = 0
        val db = new dbInstanceManager {}.queryDBInstance("data").get
        db.getOneDBAllCollectionNames.foreach(singleJobKey => {
            val maxName = UUID.randomUUID().toString
            val resultLocation = max_path_obj.p_maxPath + maxName
            delTempFile(new File(resultLocation))
            val singleJobDF = sd.mongo2RDD(mongo_config_obj.mongodbHost, mongo_config_obj.mongodbPort, mongo_config_obj.databaseName, singleJobKey).toDF()
            singleJobDF.drop("_id").write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", delimiter)
                .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                .save(resultLocation)

            rd.addMap(singleJobKey, "max_result_name", maxName)

            mongo2rddJobsCount += 1
        })
        mongo2rddJobsCount
    }

    def delTempFile(fileName: File): Unit = {
        if (fileName.isDirectory) {
            fileName.listFiles().toList match {
                case Nil => fileName.delete()
                case lstFile => lstFile.foreach(delTempFile); fileName.delete()
            }
        } else {
            fileName.delete()
        }
    }

}