package com.pharbers.delivery.astellas

import com.pharbers.delivery.util.{CommonTrait, mongo_config_obj}
import com.pharbers.paction.actionbase.{NULLArgs, RDDArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.driver.phSparkDriver

/**
  * Created by jeorch on 18-3-28.
  */
object phDeliveryReadMongoAction {
    def apply(company: String, dbName: String, lstColl: List[String]): pActionTrait =
        new phDeliveryReadMongoAction(company, dbName, lstColl)
}

class phDeliveryReadMongoAction(company: String, dbName: String, lstColl: List[String]) extends pActionTrait with CommonTrait {

    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {
        val spark = phSparkDriver()
        val lstRdd = lstColl.map(tempColl => spark.mongo2RDD(mongo_config_obj.mongoHost, mongo_config_obj.mongoPort, dbName, tempColl).rdd)
        RDDArgs(unionRDDList(lstRdd))
    }

}
