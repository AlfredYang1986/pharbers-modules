package com.pharbers.unitTest

import akka.actor.{Actor, Props}
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs}

object UnitTestHeader {
    def props(): Props = Props[UnitTestHeader]
    case class testJob(args: Map[String, String])
}

class UnitTestHeader extends Actor {
    override def receive: Receive = {
        case UnitTestHeader.testJob(args) =>
            sender() ! resultCheckJob(args)(this)
                    .perform(MapArgs(Map().empty))
                    .asInstanceOf[MapArgs]
                    .get("checkResult")
                    .asInstanceOf[StringArgs].get
    }
}