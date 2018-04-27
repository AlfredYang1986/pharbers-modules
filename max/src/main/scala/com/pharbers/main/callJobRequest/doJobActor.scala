package com.pharbers.main.callJobRequest

import play.api.libs.json.JsValue
import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.nhwa.phNhwaPanelJob

/**
  * Created by spark on 18-4-26.
  */
object doJobActor {
    def props = Props[doJobActor]
    def name = "doJob"

    case class msg_doYmCalc(js: JsValue)
    case class msg_doPanel(js: JsValue)
    case class msg_doCalc(js: JsValue)
    case class msg_doKill(js: JsValue)
}

class doJobActor extends Actor with ActorLogging {
    import com.pharbers.main.callJobRequest.doJobActor._

    override def receive = {
        case msg_doYmCalc(jv) => doYmCalc(jv)
        case msg_doPanel(jv) => doPanel(jv)
        case msg_doCalc(jv) => doCalc(jv)
        case msg_doKill(jv) => doKill(jv)
        case _ => ???
    }

    def doYmCalc(jv: JsValue): Unit = {

        val args = (jv \ "args").asOpt[String].get
                    .tail.init
                    .split(",").map(_.split("="))
                    .map(x => x.head -> x.last)
                    .toMap
        val result = args("company") match {
            case "nhwa" => phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
            case _ => ???
        }
        println("args = " + args.toString)
        println("计算月份完成")

    }

    def doPanel(jv: JsValue): Unit = {
        val args =
            (jv \ "args").asOpt[String].get
                    .tail.init
                    .split(",").map(_.split("="))
                    .map(x => x.head -> x.last)
                    .toMap

        //        val result = args("company") match {
        //            case "nhwa" => phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
        //            case _ => ???
        //        }

        println("args = " + args)
        println("生成panel完成")
    }

    def doCalc(jv: JsValue): Unit = {
        val args =
            (jv \ "args").asOpt[String].get
                    .tail.init
                    .split(",").map(_.split("="))
                    .map(x => x.head -> x.last)
                    .toMap

        //        val result = args("company") match {
        //            case "nhwa" => phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
        //            case _ => ???
        //        }

        println("args = " + args)
        println("计算完成")
    }

    def doKill(jv: JsValue): Unit = ???
}
