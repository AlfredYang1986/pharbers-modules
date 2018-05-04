package com.pharbers.common.cmd.pycmd

import com.pharbers.common.cmd.alShellPythonCmdExce

/**
  * Created by qianpeng on 2017/5/13.
  */
case class pyCmd(pyDir: String, pyFileName: String, arg: String) extends alShellPythonCmdExce {
	override def cmd = "python " + PyConfig(pyDir, pyFileName, Some(arg)).toArgs
}