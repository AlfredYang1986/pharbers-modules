package com.pharbers

import org.zalando.jsonapi.JsonapiRootObjectWriter
import org.zalando.jsonapi.model.JsonApiObject.StringValue
import org.zalando.jsonapi.model.{ Attribute, RootObject }
import org.zalando.jsonapi.model.RootObject.ResourceObject

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import io.circe.syntax._

/**
  * Class Person is used during testing
  * @param id
  * @param name
  */
@JsonCodec case class Person(id: Int, name: String)

//object Person {
//    implicit val personJsonapiRootObjectWriter: JsonapiRootObjectWriter[Person] = new JsonapiRootObjectWriter[Person] {
//        override def toJsonapi(person: Person) = {
//            RootObject(data = Some(ResourceObject(
//                `type` = "person",
//                id = Some(person.id.toString),
//                attributes = Some(List(
//                    Attribute("name", StringValue(person.name))
//                )), links = None)))
//        }
//    }
//}
