package com.pharbers.delivery.astellas

import com.pharbers.delivery.util.{CommonTrait, mongo_config_obj}
import com.pharbers.paction.actionbase.{NULLArgs, RDDArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.phSparkDriver
import org.bson.Document

/**
  * Created by jeorch on 18-3-28.
  */
object phReadMongo2RDDAction {
    def apply(company: String, dbName: String, lstColl: List[String]): pActionTrait =
        new phReadMongo2RDDAction(company, dbName, lstColl)

    def apply(company: String, dbName: String, lstColl: List[String], nickname: String): pActionTrait = {
        val temp = new phReadMongo2RDDAction(company, dbName, lstColl)
        temp.name = nickname
        temp
    }
}

class phReadMongo2RDDAction(company: String, dbName: String, lstColl: List[String]) extends pActionTrait with CommonTrait {

    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {
        val spark = phSparkDriver()
        val lstRdd = lstColl.map(tempColl =>spark.mongo2RDD(mongo_config_obj.mongoHost, mongo_config_obj.mongoPort, dbName, tempColl.split("##").head).map(doc => doc.append("Market",s"${tempColl.split("##").last}")))
        RDDArgs(unionRDDList(lstRdd))
    }

}
