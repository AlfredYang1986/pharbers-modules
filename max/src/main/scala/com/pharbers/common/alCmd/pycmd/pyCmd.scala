package com.pharbers.common.alCmd.pycmd

import com.pharbers.common.alCmd.alShellPythonCmdExce

/**
  * Created by qianpeng on 2017/5/13.
  */
case class pyCmd(pyDir: String, pyFileName: String, arg: String) extends alShellPythonCmdExce {
	override def cmd = "python " + PyConfig(pyDir, pyFileName, Some(arg)).toArgs
}