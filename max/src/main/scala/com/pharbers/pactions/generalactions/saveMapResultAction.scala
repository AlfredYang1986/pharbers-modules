package com.pharbers.pactions.generalactions

import java.io.File
import com.pharbers.pactions.actionbase._

/**
  * Created by spark on 18-3-30.
  */
object saveMapResultAction {
    def apply[T](key: String, path: String, suffix: String): pActionTrait = new saveMapResultAction[T](key, path, suffix)
}

class saveMapResultAction[T](key: String, path: String, suffix: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(prMap: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        val rdd = prMap.asInstanceOf[MapArgs].get.get(key) match {
            case Some(r) => r.asInstanceOf[RDDArgs[T]].get
            case None => ???
        }
        rdd.coalesce(1).saveAsTextFile(path)
        val tempFile = getAllFile(path).find(x => !x.endsWith("crc") && x.contains("part-")) match {
            case None => throw new Exception("not single file")
            case Some(file) => file
        }
        new File(tempFile).renameTo(new File(path + suffix))
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
