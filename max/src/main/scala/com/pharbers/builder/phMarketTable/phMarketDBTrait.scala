package com.pharbers.builder.phMarketTable

import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import com.pharbers.cliTraits.DBTrait
import play.api.libs.json.Json.toJson
import com.pharbers.builder.phMarketTable.MongoDBPool._

trait phMarketDBTrait {
    val version: String = "3.0.1" // 暂未使用
    val coll_name = "market_table"
    val db: DBTrait = MongoPool.queryDBInstance("market").get

    def dbOutput(obj: DBObject): Map[String, JsValue] = {
        obj.map { cell =>
            cell._2 match {
                case id: ObjectId => cell._1 -> toJson(id.toString)
                case str: String => cell._1 -> toJson(str)
                case obj: DBObject => cell._1 -> toJson(dbOutput(obj))
                case lst: List[DBObject] => cell._1 -> toJson(lst.map(dbOutput))
            }
        }.toMap
    }

    def insertMarketTable(obj: DBObject): Unit = db.insertObject(obj, coll_name, "_id")

    def removeMarketTable(obj: DBObject): Unit = db.deleteObject(obj, coll_name, "_id")

    def removeMultiMarketTable(obj: DBObject): Unit = db.deleteMultiObject(obj, coll_name)

    def updateMarketTable(obj: DBObject): Unit = db.updateObject(obj, coll_name, "_id")

    def queryMarketTable(condition: DBObject): Option[Map[String, JsValue]] =
        db.queryObject(condition, coll_name)(dbOutput)

    def queryMultipMarketTable(condition: DBObject): List[Map[String, JsValue]] =
        db.queryMultipleObject(condition, coll_name, "company", 0, 0)(dbOutput)

}
