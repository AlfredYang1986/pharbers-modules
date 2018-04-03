package com.pharbers.calc.alCalcHelp.alFinalDataProcess

import java.io.File

import com.pharbers.calc.alCalcHelp.alLog.alTempLog

/**
  * Created by jeorch on 2017/11/14.
  *     Modify by clock on 2018.1.15
  */
case class alRestoreColl() {
    def restore(temp_coll: String, bsonpath: String): Boolean = {
        var first: Boolean = false
        val bson_path = alBsonPath().bson_file_path
        val file = new File(bson_path + bsonpath)
        val fileList = file.listFiles()
        alTempLog(s"restore bson path = ${bson_path + bsonpath}")
        alTempLog(s"restore bson files count = ${fileList.length}")

        try {
//            openShard(temp_coll, "hashed")
            openIndex(temp_coll)

            //TODO : 破坏封装性 重写
//            fileList.foreach(x => dbrestoreCmd3(db1, temp_coll, x.toString, dbuser, dbpwd, dbhost, dbport.toInt).excute)
            true
        } catch {
            case ex: Exception =>
                println(s".....>> ${ex.getMessage}")
                false
        }
    }

    private def openIndex(coll: String) ={
//        dbc.getCollection(coll).createIndex(MongoDBObject("hosp_Index" -> 1))
//        dbc.getCollection(coll).createIndex(MongoDBObject("City" -> 1))
//        dbc.getCollection(coll).createIndex(MongoDBObject("Date" -> 1))
//        dbc.getCollection(coll).createIndex(MongoDBObject("Market" -> 1))
//        dbc.getCollection(coll).createIndex(MongoDBObject("Date" -> 1, "Market" -> 1))
//        dbc.getCollection(coll).createIndex(MongoDBObject("Date" -> 1, "Market" -> 1, "City" -> 1))
    }

    private def openShard(coll: String, shardRules: String) ={
//        val dbname = db1
//        dbc.getCollection(coll).createIndex(MongoDBObject("_id" -> shardRules))
//        dba.command(
//            DBObject(
//                "shardcollection" -> s"$dbname.$coll",
//                "key" -> DBObject(
//                    "_id" -> shardRules
//                )
//            )
//        )
    }
}