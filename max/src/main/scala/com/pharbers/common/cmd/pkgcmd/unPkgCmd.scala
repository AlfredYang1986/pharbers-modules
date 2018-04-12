package com.pharbers.common.cmd.pkgcmd

import com.pharbers.common.cmd.alShellOtherCmdExce

/**
  * Created by Alfred on 10/03/2017.
  */
case class unPkgCmd(compress_file : String, des_dir : String) extends alShellOtherCmdExce{
  override def cmd = s"tar -xzvf ${compress_file}.tar.gz -C ${des_dir}"
}