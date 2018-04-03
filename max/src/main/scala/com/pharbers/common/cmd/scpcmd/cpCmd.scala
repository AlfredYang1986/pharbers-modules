package com.pharbers.common.cmd.scpcmd

import com.pharbers.common.cmd.alShellOtherCmdExce

/**
  * Created by Alfred on 10/03/2017.
  */
case class cpCmd(file : String, des_path : String) extends alShellOtherCmdExce{
  override def cmd = s"cp ${file} ${des_path}"
}
