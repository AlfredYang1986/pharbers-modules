package com.pharbers.builder.phMarketTable

import com.mongodb.casbah.Imports._
import com.pharbers.cliTraits.DBTrait
import com.pharbers.dbManagerTrait.dbInstanceManager
import play.api.libs.json.{JsString, JsValue}

trait phMarketDB { this: phMarketTrait =>

    def getAllCompanies(): List[Map[String, String]] = {
        db.queryMultipleObject(DBObject(), "company_table")(dbOutput).map{x =>
            x.map(y => y._1 -> y._2.asInstanceOf[JsString].value)
        }
    }

    // 获取全部公司信息
    val mt2sc: Map[String, JsValue] => Map[String, String] = { mjv =>
???

    }

    val mt2des: Map[String, JsValue] => Map[String, String] = { mjv =>
        Map(
            "company" -> mjv("company").asInstanceOf[JsString].value
        )
    }

    def queryMatchFileDes(condition: DBObject)
                         (func: Map[String, JsValue] => Map[String, String]): Map[String, String] = {
        queryMultipMarketTable(condition).map(func)
        ???
    }
}