package com.pharbers.main

import com.pharbers.bmpattern.ModuleTrait
import play.api.libs.json.JsValue
import com.pharbers.bmmessages.{CommonModules, MessageDefines}
import com.pharbers.main.callMessage.msg_callback
import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.nhwa.phNhwaPanelJob

object callModule extends ModuleTrait {

    def dispatchMsg(msg: MessageDefines)(pr: Option[Map[String, JsValue]])
                   (implicit cm: CommonModules): (Option[Map[String, JsValue]], Option[JsValue]) = msg match {

        case msg_callback(jv) => (jv \ "call").asOpt[String] match {
            case Some(s) => s match{
                case "panel" => doPanel(jv)
                case _ => ???
            }
            case None => ???
        }
        case _ => ???
    }


    def doPanel(jv: JsValue)(implicit cm: CommonModules) = {
        val args =
            (jv \ "args").asOpt[String].get
                .tail.init
                .split(",").map(_.split("="))
                .map(x => x.head -> x.last)
                .toMap

        val result = args("company") match {
            case "nhwa" => phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
            case _ => ???
        }

        println("result = " + result)

        (Some(Map[String, JsValue]().empty), None)
    }

}
