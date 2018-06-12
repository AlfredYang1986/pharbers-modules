package com.pharbers.builder

import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsString, JsValue}
import play.api.libs.json.Json.toJson
import com.pharbers.dbManagerTrait.dbInstanceManager

trait MarketTable {

    private val db = new dbInstanceManager{}.queryDBInstance("calc").get
    private val coll_name = "market_table"

    implicit val dbOutput: DBObject => Map[String, JsValue] = { obj =>
        obj.keySet().toArray.map{ key =>
            key.toString -> toJson(obj.getAs[String](key.toString).getOrElse(""))
        }.toMap
    }

    implicit val jv2str: Map[String, JsValue] => Map[String, String] = jv =>
        jv.map(y => y._1 -> y._2.asInstanceOf[JsString].value)

    def insertMarketTable(obj: DBObject): Unit = db.insertObject(obj, coll_name, "_id")

    def removeMarketTable(obj: DBObject): Unit = db.deleteObject(obj, coll_name, "_id")
    def removeMultiMarketTable(obj: DBObject): Unit = db.deleteMultiObject(obj, coll_name)

    def updateMarketTable(obj: DBObject): Unit = db.updateObject(obj, coll_name, "_id")

    def queryMarketTable(condition: DBObject)
                        (implicit jv2str: Map[String, JsValue] => Map[String, String]): Option[Map[String, String]] =
        db.queryObject(condition, coll_name).map(jv2str)

    def queryMultipMarketTable(condition: DBObject)
                              (implicit jv2str: Map[String, JsValue] => Map[String, String]): List[Map[String, String]] =
        db.queryMultipleObject(condition, coll_name, "company").map(jv2str)

    val marketTable: List[Map[String, String]] = queryMultipMarketTable(DBObject())
}
