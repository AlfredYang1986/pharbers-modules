package com.pharbers.panel.common

import play.api.libs.json.Json.toJson
import com.pharbers.pactions.actionbase._

object phCalcYM2JVJobWithCpaAndGyc  {
    def apply(args: pActionArgs = NULLArgs) : pActionTrait = new phCalcYM2JVJobWithCpaAndGyc(args)
}

class phCalcYM2JVJobWithCpaAndGyc(override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = "result"
    override def perform(prMap : pActionArgs): pActionArgs = {

        val cpa = prMap.asInstanceOf[MapArgs].get("calcYMWithCpa").asInstanceOf[RDDArgs[(String, Int)]].get
        val cpaMax = cpa.map(_._2).max
        val cpaLst = cpa.filter(_._2 > cpaMax/2).map(_._1 -> 1)

        val gycx = prMap.asInstanceOf[MapArgs].get("calcYMWithGycx").asInstanceOf[RDDArgs[(String, Int)]].get
        val gycxMax = gycx.map(_._2).max
        val gycxLst = gycx.filter(_._2 > gycxMax/2).map(_._1 -> 1)

        val result = cpaLst.join(gycx).map(_._1).collect().sorted

        JVArgs(
            toJson(result.mkString("#"))
        )
    }
}