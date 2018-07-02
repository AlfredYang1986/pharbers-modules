package com.pharbers.unitTest.action

import java.util.UUID

import com.pharbers.pactions.actionbase._
import org.apache.spark.sql.DataFrame

object writeCheckResultAction {
    def apply(args: MapArgs): pActionTrait = new writeCheckResultAction(args)
}

class writeCheckResultAction(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "checkResult"

    override def perform(args : pActionArgs): pActionArgs = {
        val totalResult: DataFrame = args.asInstanceOf[MapArgs].get("result_check").asInstanceOf[DFArgs].get
        val path = "/mnt/config/result/"
        val uuid = UUID.randomUUID().toString
        totalResult.coalesce(1).write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .save(path + uuid)
        println("单个市场单元测试：" + uuid)
        StringArgs(uuid)
    }
}
