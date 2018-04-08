package com.pharbers.panel.common

import java.io.File
import com.pharbers.panel.panel_path_obj
import com.pharbers.pactions.actionbase._

object phSavePanelJob  {
    def apply(args: MapArgs) : pActionTrait = new phSavePanelJob(args)
}

class phSavePanelJob(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "phSavePanelJob"
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(pr : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        val panel = pr.asInstanceOf[MapArgs].get("panel").asInstanceOf[DFArgs].get
        val temp_name = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val panel_name = temp_name + ".csv"
        val temp_panel_dir = panel_path_obj.p_resultPath + temp_name
        val panel_location = panel_path_obj.p_resultPath + panel_name

        def getAllFile(dir: String): Array[String] = {
            val list = new File(dir).listFiles()
            list.flatMap {file =>
                if (file.isDirectory) {
                    getAllFile(file.getAbsolutePath)
                } else {
                    Array(file.getAbsolutePath)
                }
            }
        }

        def delFile(dir: String): Unit = {
            val parent = new File(dir)
            val list = parent.listFiles()
            list.foreach {file =>
                if (file.isDirectory) {
                    delFile(file.getAbsolutePath)
                } else {
                    file.delete()
                }
            }
            parent.delete()
        }

        panel.coalesce(1).write
                .format("csv")
                .option("delimiter", 31.toChar.toString)
                .save(temp_panel_dir)

        val tempFile = getAllFile(temp_panel_dir).find(_.endsWith(".csv")) match {
            case None => throw new Exception("not single file")
            case Some(file) => file
        }
        new File(tempFile).renameTo(new File(panel_location))
        delFile(temp_panel_dir)

        StringArgs(temp_name)
    }

}