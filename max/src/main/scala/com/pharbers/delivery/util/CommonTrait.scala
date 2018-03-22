package com.pharbers.delivery.util

import java.io.File

import com.pharbers.spark.driver.phSparkDriver
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.DataFrame

/**
  * Created by jeorch on 18-3-7.
  */
trait CommonTrait {

    val driver =  phSparkDriver()

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
        FileUtils.copyFile(originFile, destFile)
    }

    def unionDataFrameList(listDF: List[DataFrame]): DataFrame = {
        listDF.length match {
            case 1 => listDF.head
            case 2 => listDF.head.union(listDF.last)
            case _ : Int => listDF.head.union(unionDataFrameList(listDF.tail))
            // case _ => throw new Exception("unionDataFrameList Error!")
        }
    }

    def closeSparkSession = driver.ss.stop()

}
