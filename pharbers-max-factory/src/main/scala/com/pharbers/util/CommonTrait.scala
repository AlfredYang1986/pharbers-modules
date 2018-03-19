package com.pharbers.util

import java.io.File

import org.apache.commons.io.FileUtils

/**
  * Created by jeorch on 18-3-7.
  */
trait CommonTrait {

    def getResultFileFullPath(arg: String) : String = {

        val folder = new File(arg)
        val listFile = folder.listFiles().filter(x => x.getName.endsWith(".csv"))
        listFile.length match {
            case 1 => listFile.head.getAbsolutePath
            case _ => throw new Exception("not single file")
        }
    }

    def move2ExportFolder(originPath: String, destPath: String) = {
        val originFile = new File(originPath)
        val destFile = new File(destPath)
        FileUtils.copyFile(originFile, destFile)
    }

}
