package com.pharbers.delivery.nhwa

import java.util.UUID

import com.pharbers.paction.actionbase.{NULLArgs, pActionArgs, pActionTrait}

/**
  * Created by jeorch on 18-3-28.
  */
object phNhwaDeliveryAction {
    def apply(company: String, dbName: String, lstColl: List[String], destPath: String): pActionTrait =
        new phNhwaDeliveryAction(company, dbName, lstColl, destPath)
}

class phNhwaDeliveryAction (company: String, dbName: String, lstColl: List[String],
                            destPath: String) extends pActionTrait {
    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        val driverNHWA = DriverNHWA()
        val listDF = lstColl.map(temp => driverNHWA.generateDeliveryFileFromMongo(s"$dbName", s"$temp"))
        val originFilePath = driverNHWA.save2File(driverNHWA.unionDataFrameList(listDF))
        val uuid = UUID.randomUUID().toString
        val fileName = s"${company}-${uuid}.csv"
        driverNHWA.move2ExportFolder(originFilePath, s"${destPath}/${fileName}")
        driverNHWA.closeSparkSession

        defaultArgs
    }
}
