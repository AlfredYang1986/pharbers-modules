package com.pharbers.calc

import java.io.File

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.delivery.util.mongo_config_obj
import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver

/**
  * Created by jeorch on 18-5-15.
  */
case class phMaxScheduleJob(user: String) {

    private val maxJobsKey = Sercurity.md5Hash("Pharbers")

    def start: Int = {
        val rd = new PhRedisDriver()
        val sd = phSparkDriver()
        val admin = rd.getString("MaxAdmin")
        var scheduledJobsCount = 0
        if(user == admin){
            rd.getSetAllValue(maxJobsKey).foreach(singleJobKey => {
                val maxName = rd.getMapValue(singleJobKey, "max_result_name")
                val resultLocation = max_path_obj.p_maxPath + maxName
                delTempFile(new File(resultLocation))
                val singleJobDF = sd.mongo2RDD(mongo_config_obj.mongodbHost, mongo_config_obj.mongodbPort, mongo_config_obj.databaseName, singleJobKey).toDF()
                singleJobDF.drop("_id").write
                    .format("csv")
                    .option("header", value = true)
                    .option("delimiter", 31.toChar.toString)
                    .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                    .save(resultLocation)
                scheduledJobsCount += 1
            })
        } else scheduledJobsCount = -1
        scheduledJobsCount
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
