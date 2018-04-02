package com.pharbers.common.alCmd.pycmd

import com.pharbers.common.alFileHandler.fileConfig._

case class PyConfig(pyDir: String, pyFileName: String, arg: Option[String] = None) {
    def toArgs: String = s"$pyDir$python$pyFileName $pyDir ${arg.getOrElse("")}"
}