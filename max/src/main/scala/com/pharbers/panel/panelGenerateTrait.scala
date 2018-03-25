package com.pharbers.panel

import com.pharbers.paction.actionbase._
import com.pharbers.spark.driver.phSparkDriver
import com.pharbers.panel.nhwa.phNhwahandle2
import play.api.libs.json.JsValue

object panelGenerateTrait {
    def apply(args : pActionArgs) : pActionTrait = new panelGenerateTrait(args)
}

class panelGenerateTrait(override val defaultArgs: pActionArgs) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

       def getResult(data: JsValue) = {
            data.as[Map[String, JsValue]].map { x =>
                x._1 -> x._2.as[Map[String, JsValue]].map { y =>
                    y._1 -> y._2.as[List[String]]
                }
            }
        }

        val sc = phSparkDriver()
        val panelHander = new phNhwahandle2(sc)
//        val result = getResult(panelHander.getPanelFile(List("201701"), "麻醉市场"))
//        println(result)

        panelHander.getPanelFile(List("201701"), "麻醉市场")
        NULLArgs
    }
}