package com.pharbers.delivery.util

import java.io.File

import com.pharbers.spark.phSparkDriver
import org.apache.commons.io.FileUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

/**
  * Created by jeorch on 18-3-7.
  */
trait CommonTrait {

    lazy val driver =  phSparkDriver()

    def getResultFileFullPath(arg: String) : String = {
        val folder = new File(arg)
        val listFile = folder.listFiles().filter(x => x.getName.endsWith(".csv"))
        listFile.length match {
            case 1 => listFile.head.getAbsolutePath
            case _ => listFile.sortBy(x => x.lastModified()).last.getAbsolutePath
        }
    }

    def move2ExportFolder(originPath: String, destPath: String) = {
        val originFile = new File(originPath)
        val destFile = new File(destPath)
        FileUtils.write(destFile, FileUtils.readFileToString(originFile), "GB2312")
    }

    def unionDataFrameList(listDF: List[DataFrame]): DataFrame = {
        listDF match {
            case Nil => throw new Exception("Empty DataFrameList! unionDataFrameList Error!")
            case head::Nil => head
            case head::tail => head.union(unionDataFrameList(tail))
        }
    }

    def unionRDDList[T](listRDD: List[RDD[T]]): RDD[T] = {
        listRDD match {
            case Nil => throw new Exception("Empty RDDList! unionRDDList Error!")
            case head::Nil => head
            case head::tail => head.union(unionRDDList(tail))
        }
    }

    def closeSparkSession = driver.ss.stop()

}
