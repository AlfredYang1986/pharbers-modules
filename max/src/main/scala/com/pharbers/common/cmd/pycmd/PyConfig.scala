package com.pharbers.common.cmd.pycmd

case class PyConfig(pyDir: String, pyFileName: String, arg: Option[String] = None) {
    def toArgs: String = "未实现"//s"$pyDir$python$pyFileName $pyDir ${arg.getOrElse("")}"
}