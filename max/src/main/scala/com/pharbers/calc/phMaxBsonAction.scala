package com.pharbers.calc

import java.util.UUID

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase._

object phMaxBsonAction {
    def apply[T](args: pActionArgs = NULLArgs): pActionTrait = new phMaxBsonAction[T](args)
}

class phMaxBsonAction[T](override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_bson_action"

    override def perform(prMap: pActionArgs): pActionArgs = {

        val max_result = prMap.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val result_name = UUID.randomUUID().toString
        val result_location = max_path_obj.p_resultPath + result_name

        max_result.write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                .save(result_location)

        StringArgs(result_name)
    }
}