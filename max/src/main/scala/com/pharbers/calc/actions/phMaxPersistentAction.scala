package com.pharbers.calc.actions

import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase._

object phMaxPersistentAction {
    def apply[T](args: pActionArgs = NULLArgs): pActionTrait = new phMaxPersistentAction[T](args)
}

class phMaxPersistentAction[T](override val defaultArgs: pActionArgs) extends pActionTrait{
    override val name: String = "max_persistent_action"

    override def perform(prMap: pActionArgs): pActionArgs = {

        val max_result = prMap.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val panelName = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val result_location = max_path_obj.p_maxPath + panelName

        max_result.write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                .save(result_location)

        StringArgs(panelName)
    }
}