package com.pharbers.calc

import com.pharbers.pactions.actionbase._
import com.pharbers.panel.panel_path_obj

object phMaxBsonAction {
    def apply[T](args: pActionArgs = NULLArgs): pActionTrait = new phMaxBsonAction[T](args)
}

class phMaxBsonAction[T](override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_bson_action"
    override implicit def progressFunc(progress: Double, flag: String) : Unit = {}

    override def perform(prMap: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        val max_result = prMap.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val result_location = panel_path_obj.p_resultPath + "test_max_result"

        max_result.coalesce(5).write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .save(result_location)

        StringArgs("OK")

    }

}