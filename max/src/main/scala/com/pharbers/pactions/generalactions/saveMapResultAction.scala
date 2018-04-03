package com.pharbers.pactions.generalactions

import java.io.File
import java.util.UUID
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.PhExcelWritable

/**
  * Created by spark on 18-3-30.
  */
object saveMapResultAction {
    def apply(key: String, path: String): pActionTrait = new saveMapResultAction(key, path)
}

class saveMapResultAction(key: String, path: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(prMap: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        key match {
            case "panelResult" => {
                val rdd = prMap.asInstanceOf[MapArgs].get.get(key) match {
                    case Some(r) => r.asInstanceOf[RDDArgs[PhExcelWritable]].get
                    case None => ???
                }
                rdd.coalesce(1).saveAsTextFile(path)
                val tempFile = getAllFile(path).find(x => !x.endsWith("crc") && x.contains("part-")) match {
                    case None => throw new Exception("not single file")
                    case Some(file) => file
                }
                new File(tempFile).renameTo(new File(path + ".csv"))
            }
            case "deliveryResult" => {
                val rdd = prMap.asInstanceOf[MapArgs].get.get(key) match {
                    case Some(r) => r.asInstanceOf[RDDArgs[String]].get
                    case None => ???
                }
                val yearMonth = rdd.take(2)(1).split(9.toChar.toString)(2)
                rdd.coalesce(1).saveAsTextFile(path)
                val tempFile = getAllFile(path).find(x => !x.endsWith("crc") && x.contains("part-")) match {
                    case None => throw new Exception("not single file")
                    case Some(file) => file
                }
                new File(tempFile).renameTo(new File(path + yearMonth + ".txt"))
            }
            case _ => throw new Exception("no key match!")
        }

        delFile(path)

        prMap
    }

    private def getAllFile(dir: String): Array[String] = {
        val list = new File(dir).listFiles()
        list.flatMap { file =>
            if (file.isDirectory) {
                getAllFile(file.getAbsolutePath)
            } else {
                Array(file.getAbsolutePath)
            }
        }
    }

    private def delFile(dir: String): Unit = {
        val parent = new File(dir)
        val list = parent.listFiles()
        list.foreach { file =>
            if (file.isDirectory) {
                delFile(file.getAbsolutePath)
            } else {
                file.delete()
            }
        }
        parent.delete()
    }
}
