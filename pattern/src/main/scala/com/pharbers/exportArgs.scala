package com.pharbers.export

import com.pharbers.Person
import org.zalando.jsonapi.JsonapiRootObjectWriter
import org.zalando.jsonapi.model._
import org.zalando.jsonapi.model.JsonApiObject.StringValue
import org.zalando.jsonapi.model.RootObject.ResourceObject


import org.zalando.jsonapi.json.playjson.PlayJsonJsonapiSupport._
import play.api.libs.json._

class exportArgs {

    val json =
        """
          {
            "data": {
              "id": "42",
              "type": "person",
              "attributes": {
                "name": "foobar"
              },
              "links": {
                "self": "http://test.link/person/42"
              }
            }
          }
        """.stripMargin

    val tmp = Json.parse(json)
}
