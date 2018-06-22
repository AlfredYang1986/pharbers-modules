package com.pharbers.calc

import java.io.File
import java.util.UUID

import com.pharbers.common.algorithm.{max_data_sync_mongo_obj, max_path_obj}
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase.NULLArgs
import com.pharbers.pactions.generalactions.jarPreloadAction
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver

trait phMaxScheduleTrait {

    private val maxSingleDayJobsKey = Sercurity.md5Hash("Pharbers")
    private val delimiter = 31.toChar.toString

    def rdd2mongo: Int = {
        val rd = new PhRedisDriver()
        val sd = phSparkDriver()
        var rdd2mongoJobsCount = 0
        jarPreloadAction().perform(NULLArgs)
        rd.getSetAllValue(maxSingleDayJobsKey).foreach(singleJobKey => {
            val maxName = rd.getMapValue(singleJobKey, "max_result_name")
            val resultLocation = max_path_obj.p_maxPath + maxName
            val singleJobDF = sd.csv2RDD(resultLocation, delimiter)
            singleJobDF.write.format("com.mongodb.spark.sql.DefaultSource").mode("overwrite")
                .option("uri", s"mongodb://${max_data_sync_mongo_obj.mongodbHost}:${max_data_sync_mongo_obj.mongodbPort}/")
                .option("database", max_data_sync_mongo_obj.databaseName)
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
        jarPreloadAction().perform(NULLArgs)
        db.getOneDBAllCollectionNames.foreach(singleJobKey => {
            val maxNameForSearch = UUID.randomUUID().toString
            val resultLocation = max_path_obj.p_maxPath + maxNameForSearch
            delTempFile(new File(resultLocation))
            val singleJobDF = sd.mongo2RDD(max_data_sync_mongo_obj.mongodbHost, max_data_sync_mongo_obj.mongodbPort, max_data_sync_mongo_obj.databaseName, singleJobKey).toDF()
            singleJobDF.groupBy("Date", "Province", "City", "MARKET", "Product")
                .agg(Map("f_sales"->"sum", "f_units"->"sum", "Panel_ID"->"first"))
                .write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", delimiter)
                .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                .save(resultLocation)

            rd.addMap(singleJobKey, "max_result_name_for_search", maxNameForSearch)
            rd.expire(singleJobKey, 24 * 60 * 60)
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
