package com.pharbers.jsonapi

import com.pharbers.jsonapi.model.Links.Link
import com.pharbers.jsonapi.model.RootObject.ResourceObjects

import scala.collection.immutable.{Seq ⇒ ImmutableSeq}

/**
  * The model package, containing partially covered Jsonapi specification.
  */
package object model {

  /**
    * A root, top-level object.
    */
  case class RootObject(data: Option[RootObject.Data] = None,
                        links: Option[Links] = None,
                        errors: Option[Errors] = None,
                        meta: Option[Meta] = None,
                        included: Option[Included] = None,
                        jsonApi: Option[JsonApi] = None)

  /**
    * A companion object for root level support types.
    */
  object RootObject {
    sealed trait Data

    case class ResourceObject(`type`: String,
                              id: Option[String] = None,
                              attributes: Option[Attributes] = None,
                              relationships: Option[Relationships] = None,
                              links: Option[Links] = None,
                              meta: Option[Meta] = None)
        extends Data

    /**
      * A collection of [[ResourceObject]] objects.
      */
    case class ResourceObjects(array: ImmutableSeq[ResourceObject]) extends Data
  }

  /**
    * A collection of [[Link]] objects.
    */
  type Links = ImmutableSeq[Link]

  /**
    * Companion object for links.
    */
  object Links {
    sealed trait Link

    /**
      * A link of the "self" type.
      * @param url The url to link to.
      * @param meta The optional meta to link to.
      */
    case class Self(url: String, meta: Option[Meta]) extends Link

    /**
      * A link of the "related" type.
      * @param url The url to link to.
      * @param meta The optional meta to link to.
      */
    case class Related(url: String, meta: Option[Meta]) extends Link

    /**
      * A link of the "first" type.
      * @param url The url to link to.
      * @param meta The optional meta to link to.
      */
    case class First(url: String, meta: Option[Meta]) extends Link

    /**
      * A link of the "last" type.
      * @param url The url to link to.
      * @param meta The optional meta to link to.
      */
    case class Last(url: String, meta: Option[Meta]) extends Link

    /**
      * A link of the "next" type.
      * @param url The url to link to.
      * @param meta The optional meta to link to.
      */
    case class Next(url: String, meta: Option[Meta]) extends Link

    /**
      * A link of the "prev" type.
      * @param url The url to link to.
      * @param meta The optional meta to link to.
      */
    case class Prev(url: String, meta: Option[Meta]) extends Link

    /**
      * A link of the "about" type.
      * @param url The url to link to.
      * @param meta The optional meta to link to.
      */
    case class About(url: String, meta: Option[Meta]) extends Link
  }

  /**
    * A collection of [[Attribute]] objects.
    */
  type Attributes = ImmutableSeq[Attribute]

  /**
    * The representation of an attribute of the root object.
    * @param name the name of the attribute
    * @param value the value of the attribute
    */
  case class Attribute(name: String, value: JsonApiObject.Value)

  /**
    * A collection of [[Error]] objects.
    */
  type Errors = ImmutableSeq[Error]

  /**
    * The representation of an error object.
    * @param id an unique identifier of the error
    * @param links the links of the error
    * @param status the HTTP status code of the error
    * @param code an application specific code of the error
    * @param title a short human-readable description about the error
    * @param detail a detailed human-readable description about the error
    * @param source the source of the error
    * @param meta the meta information about the error
    */
  case class Error(id: Option[String] = None,
                   links: Option[Links] = None,
                   status: Option[String] = None,
                   code: Option[String] = None,
                   title: Option[String] = None,
                   detail: Option[String] = None,
                   source: Option[ErrorSource] = None,
                   meta: Option[Meta] = None)

  /**
    * An object containing references to the source of the error.
    * @param pointer the optional pointer based on <a href="https://tools.ietf.org/html/rfc6901">RFC6901</a> standard
    * @param parameter a optional string indicating which URI query parameter caused the error
    */
  case class ErrorSource(pointer: Option[String] = None, parameter: Option[String] = None)

  /**
    * The meta object as a map of string - json object value pairs
    */
  type Meta = Map[String, JsonApiObject.Value]

  /**
    * An array of resource objects.
    * @param resourceObjects the array
    */
  case class Included(resourceObjects: ResourceObjects)

  /**
    * A collection of [[JsonApiProperty]] objects.
    */
  type JsonApi = ImmutableSeq[JsonApiProperty]

  /**
    * An information about the implementation.
    * @param name the name of the json api implementation detail
    * @param value the value of the json api implementation detail
    */
  case class JsonApiProperty(name: String, value: JsonApiObject.Value)

  /**
    * A companion object for json api implementation specific data.
    */
  object JsonApiObject {

    sealed trait Value

    /**
      * An attribute value that is string-typed.
      * @param value the string value
      */
    case class StringValue(value: String) extends Value

    /**
      * An attribute value that is number-typed.
      * @param value the number value
      */
    case class NumberValue(value: BigDecimal) extends Value

    /**
      * An attribute value that is boolean-typed.
      * @param value the boolean value
      */
    case class BooleanValue(value: Boolean) extends Value

    /**
      * An attribute value that is list(key, value)-typed.
      * @param value the list of key-value pairs
      */
    case class JsObjectValue(value: Attributes) extends Value

    /**
      * An attribute value that is array-typed.
      * @param value the array value
      */
    case class JsArrayValue(value: Seq[Value]) extends Value

    /**
      * An attribute value that is null.
      */
    case object NullValue extends Value

    /**
      * An attribute value that is true
      */
    val TrueValue = BooleanValue(true)

    /**
      * An attribute value that is false
      */
    val FalseValue = BooleanValue(false)
  }

  /**
    * A collection of [[Relationship]] objects.
    */
  type Relationships = Map[String, Relationship]

  /**
    * An object represents the relationship and describes underlying object.
    * @param links the links of underlying object
    * @param data the data of underlying object
    */
  case class Relationship(links: Option[Links] = None, data: Option[RootObject.Data] = None)

}
