package com.pharbers.calc.old.alMSA.alMaxCmdJob

import akka.actor.{Actor, ActorLogging, Props}
import com.pharbers.calc.old.alCalcHelp.alLog.alTempLog
import com.pharbers.calc.old.alMSA.alCalcMaster.alCalcMsg.scpMsg._
import com.pharbers.common.cmd.scpcmd.scpCmd
import com.pharbers.common.cmd.pkgcmd.{pkgCmd, unPkgCmd}

object alCmdActor {
	def props() = Props[alCmdActor]
}

class alCmdActor extends Actor with ActorLogging {
	override def receive: Receive = {
		case pkgmsg(file, target) => {
			pkgCmd(file, target).excute
			sender() ! pkgend(self)
		}

		case pkgmsgMuti(targets) => {
			targets.foreach(x => pkgCmd(x("file") :: Nil, x("target")).excute)
			sender() ! pkgend(self)
		}

		case scpmsg(file, target, host, user) => {
			scpCmd(file, target, host, user).excute
			sender() ! scpend(self)
		}

		case scpmsgMutiPath(targets, host, user) => {
			targets.foreach(x => scpCmd(x("file"), x("target"), host, user).excute)
			sender() ! scpend(self)
		}

		case unpkgmsg(target, des_dir, s) => {
			unPkgCmd(target, des_dir).excute
			sender() ! unpkgend(s)
		}

		case unpkgmsgMutiPath(targets, des_dir, s) => {
			targets.foreach(target => unPkgCmd(target, des_dir).excute)
			sender() ! unpkgend(s)
		}

		case msg : AnyRef => alTempLog(s"Warning! Message not delivered. alCmdActor.received_msg=$msg")
	}
}
