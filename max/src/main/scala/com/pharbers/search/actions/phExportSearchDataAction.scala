package com.pharbers.search.actions

import java.util.{Date, UUID}

import com.pharbers.common.algorithm.{max_path_obj, phSparkCommonFuncTrait}
import com.pharbers.pactions.actionbase._

/**
  * Created by jeorch on 18-6-4.
  */
object phExportSearchDataAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phExportSearchDataAction(args)
}

class phExportSearchDataAction(override val defaultArgs: pActionArgs) extends pActionTrait with phSparkCommonFuncTrait {
    override val name: String = "export_search_data_action"

    override def perform(pr: pActionArgs): pActionArgs = {

        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val ym_condition = defaultArgs.asInstanceOf[MapArgs].get("ym_condition").asInstanceOf[StringArgs].get
        val maxSearchDF = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get
        val maxSearchResultName = UUID.randomUUID().toString
        val exportDataPath = max_path_obj.p_exportPath + maxSearchResultName
        val destFileName = s"${new Date().getTime}-$mkt-$ym_condition.csv"
        val destPath = max_path_obj.p_exportPath + destFileName

        maxSearchDF
            .withColumnRenamed("sum(f_sales)", "Sales")
            .withColumnRenamed("sum(f_units)", "Units")
            .drop("first(Panel_ID)")
            .coalesce(1).write
            .format("csv")
            .option("header", value = true)
            .option("delimiter", 31.toChar.toString)
            .save(exportDataPath)

        move2ExportFolder(getResultFileFullPath(exportDataPath), destPath)

        StringArgs(destFileName)
    }
}
