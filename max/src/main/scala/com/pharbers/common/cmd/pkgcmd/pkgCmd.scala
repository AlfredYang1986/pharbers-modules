package com.pharbers.common.cmd.pkgcmd

import com.pharbers.common.cmd.alShellOtherCmdExce

/**
  * Created by Alfred on 09/03/2017.
  */
case class pkgCmd(lst : List[String], compress_file : String) extends alShellOtherCmdExce{
  override val cmd = s"tar -czvf ${compress_file}.tar.gz ${lst.head}"
}

