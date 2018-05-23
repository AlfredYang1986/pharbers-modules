package com.pharbers.panel.common

import scala.reflect.ClassTag
import play.api.libs.json.Json.toJson
import com.pharbers.pactions.actionbase._

object phCalcYM2JVJob  {
    def apply[T : ClassTag](args: pActionArgs = NULLArgs): pActionTrait = {
        new phCalcYM2JVJob[T](args)
    }
}

class phCalcYM2JVJob[T : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = "result"
    override def perform(pr : pActionArgs): pActionArgs = {
        val rdd = pr.asInstanceOf[MapArgs].get("calcYM")
                .asInstanceOf[RDDArgs[(String, Int)]].get.collect()
        val maxYm = rdd.map(_._2).max
        val result = rdd.filter(_._2 > maxYm/2).map(_._1).sorted

        JVArgs(
            toJson(result.mkString("#"))
        )
    }
}