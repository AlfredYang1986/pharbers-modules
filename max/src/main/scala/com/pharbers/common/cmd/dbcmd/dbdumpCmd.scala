package com.pharbers.common.cmd.dbcmd

import com.pharbers.common.cmd.alShellOtherCmdExce

case class dbdumpCmd(db: String,
                     coll: String,
                     out: String,
                     userName: String,
                     userPassword: String,
                     ip: String= "127.0.0.1",
                     port: Int = 27017) extends alShellOtherCmdExce {
   override def cmd = "mongodump " + DBConfig(db, coll, out, Some(ip), Some(port), Some(userName), Some(userPassword)).toArgs
}