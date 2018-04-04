package com.pharbers.delivery.astellas

import com.pharbers.delivery.util.{CommonTrait, mongo_config_obj}
import com.pharbers.pactions.actionbase.{NULLArgs, RDDArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.phSparkDriver

/**
  * Created by jeorch on 18-3-28.
  */
object phReadMongo2RDDAction {
    def apply(company: String, dbName: String, lstColl: List[String], name: String): pActionTrait =
        new phReadMongo2RDDAction(company, dbName, lstColl, name)
}

class phReadMongo2RDDAction(company: String, dbName: String, lstColl: List[String], override val name: String) extends pActionTrait with CommonTrait {

    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {
        val spark = phSparkDriver()
        val lstRdd = lstColl.map(tempColl =>spark.mongo2RDD(mongo_config_obj.mongoHost, mongo_config_obj.mongoPort, dbName, tempColl.split("##").head).map(doc => doc.append("Market",s"${tempColl.split("##").last}")))
        RDDArgs(unionRDDList(lstRdd))
    }

}
