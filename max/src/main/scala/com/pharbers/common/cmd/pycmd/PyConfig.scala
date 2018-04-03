package com.pharbers.common.cmd.pycmd

import com.pharbers.common.another_file_package.fileConfig._

case class PyConfig(pyDir: String, pyFileName: String, arg: Option[String] = None) {
    def toArgs: String = s"$pyDir$python$pyFileName $pyDir ${arg.getOrElse("")}"
}