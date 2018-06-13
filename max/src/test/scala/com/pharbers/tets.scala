package com.pharbers

import org.scalatest.FunSuite
import play.api.libs.json.JsValue
import com.mongodb.casbah.Imports._
import play.api.libs.json.Json.toJson
import com.pharbers.builder.phMarketTable._

class tets extends FunSuite {
    test("aaa") {
        val marketTable = new phMarketDB with phMarketTrait {}

        val condition: DBObject = {
            DBObject("company" -> "5afa53bded925c05c6f69c54")
        }

        val a = marketTable.getAllCompanies

        println(a)

    }
}
