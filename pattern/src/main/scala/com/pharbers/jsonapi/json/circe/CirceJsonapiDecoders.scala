package com.pharbers.jsonapi.json.circe

import io.circe._
import com.pharbers.jsonapi.json.FieldNames
import com.pharbers.jsonapi.model.JsonApiObject._
import com.pharbers.jsonapi.model.RootObject._
import com.pharbers.jsonapi.model.{Errors, Error, _}

trait CirceJsonapiDecoders {
  def jsonToValue(json: Json): Value = json.fold[Value](
      NullValue,
      BooleanValue.apply,
      value ⇒ NumberValue(value.toBigDecimal.get),
      StringValue.apply,
      values ⇒ JsArrayValue(values.map(jsonToValue)),
      values ⇒
        JsObjectValue(values.toMap.map {
          case (key, value) ⇒ Attribute(key, jsonToValue(value))
        }.toList)
  )

  implicit val valueDecoder = Decoder.instance[Value](_.as[Json].right.map(jsonToValue))

  implicit val attributesDecoder = Decoder.instance[Attributes](hcursor ⇒ {
    hcursor.as[Value].right.flatMap {
      case JsObjectValue(value) ⇒
        Right(value)
      case _ ⇒
        Left(DecodingFailure("only an object can be decoded to Attributes", hcursor.history))
    }
  })

  implicit val attributeDecoder = Decoder.instance[Attribute](_.as[Attributes].right.map(_.head))

  implicit val linksDecoder = Decoder.instance[Links](hcursor ⇒ {
    hcursor.as[Value].right.flatMap {
      case JsObjectValue(attributes) ⇒
        Right(attributes.map {
          case Attribute(FieldNames.`self`, StringValue(url)) ⇒ Links.Self(url, None)
          case Attribute(FieldNames.`self`, JsObjectValue(linkAttributes)) =>
            val linkValues = attributesToLinkValues(linkAttributes)
            Links.Self(linkValues._1, linkValues._2)

          case Attribute(FieldNames.`about`, StringValue(url)) ⇒
            Links.About(url, None)
          case Attribute(FieldNames.`about`, JsObjectValue(linkAttributes)) =>
            val linkValues = attributesToLinkValues(linkAttributes)
            Links.About(linkValues._1, linkValues._2)

          case Attribute(FieldNames.`first`, StringValue(url)) ⇒
            Links.First(url, None)
          case Attribute(FieldNames.`first`, JsObjectValue(linkAttributes)) =>
            val linkValues = attributesToLinkValues(linkAttributes)
            Links.First(linkValues._1, linkValues._2)

          case Attribute(FieldNames.`last`, StringValue(url)) ⇒ Links.Last(url, None)
          case Attribute(FieldNames.`last`, JsObjectValue(linkAttributes)) =>
            val linkValues = attributesToLinkValues(linkAttributes)
            Links.Last(linkValues._1, linkValues._2)

          case Attribute(FieldNames.`next`, StringValue(url)) ⇒ Links.Next(url, None)
          case Attribute(FieldNames.`next`, JsObjectValue(linkAttributes)) =>
            val linkValues = attributesToLinkValues(linkAttributes)
            Links.Next(linkValues._1, linkValues._2)

          case Attribute(FieldNames.`prev`, StringValue(url)) ⇒ Links.Prev(url, None)
          case Attribute(FieldNames.`prev`, JsObjectValue(linkAttributes)) =>
            val linkValues = attributesToLinkValues(linkAttributes)
            Links.Prev(linkValues._1, linkValues._2)

          case Attribute(FieldNames.`related`, StringValue(url)) ⇒
            Links.Related(url, None)
          case Attribute(FieldNames.`related`, JsObjectValue(linkAttributes)) =>
            val linkValues = attributesToLinkValues(linkAttributes)
            Links.Related(linkValues._1, linkValues._2)
        })
      case _ ⇒
        Left(DecodingFailure("only an object can be decoded to Links", hcursor.history))
    }
  })

  def attributesToLinkValues(linkObjectAttributes: Attributes): (String, Option[Meta]) = {
    (linkObjectAttributes.find(_.name == "href"), linkObjectAttributes.find(_.name == "meta")) match {
      case (Some(hrefAttribute), Some(metaAttribute)) =>
        val href = hrefAttribute match {
          case Attribute("href", StringValue(url)) => url
        }
        val meta: Map[String, JsonApiObject.Value] = metaAttribute match {
          case Attribute("meta", JsObjectValue(metaAttributes)) =>
            metaAttributes.map {
              case Attribute(name, value) ⇒ name -> value
            }.toMap
        }
        (href, Some(meta))
    }
  }

  def jsonToData(json: Json): Either[DecodingFailure, Data] = json match {
    case json: Json if json.isArray ⇒
      json.as[ResourceObjects]
    case json: Json if json.isObject ⇒
      json.as[ResourceObject]
  }

  implicit val dataDecoder = Decoder.instance[Data](_.as[Json].right.flatMap(jsonToData))

  implicit val relationshipDecoder = Decoder.instance[Relationship](hcursor ⇒ {
    for {
      links ← hcursor.downField(FieldNames.`links`).as[Option[Links]].right
      data ← hcursor.downField(FieldNames.`data`).as[Option[Data]].right
    } yield
      Relationship(
          links = links,
          data = data
      )
  })

  implicit val relationshipsDecoder = Decoder.instance[Relationships](_.as[Map[String, Relationship]])

  implicit val jsonApiDecoder = Decoder.instance[JsonApi](hcursor ⇒ {
    hcursor.as[Value].right.flatMap {
      case JsObjectValue(attributes) ⇒
        Right(attributes.map {
          case Attribute(name, value) ⇒ JsonApiProperty(name, value)
        })
      case _ ⇒
        Left(DecodingFailure("only an object can be decoded to JsonApi", hcursor.history))
    }
  })

  implicit val metaDecoder = Decoder.instance[Meta](hcursor ⇒ {
    hcursor.as[Value].right.flatMap {
      case JsObjectValue(attributes) ⇒
        Right(attributes.map {
          case Attribute(name, value) ⇒ name -> value
        }.toMap)
      case _ ⇒
        Left(DecodingFailure("only an object can be decoded to Meta", hcursor.history))
    }
  })

  implicit val errorSourceDecoder = Decoder.instance[ErrorSource](hcursor ⇒ {
    for {
      pointer ← hcursor.downField(FieldNames.`pointer`).as[Option[String]].right
      parameter ← hcursor.downField(FieldNames.`parameter`).as[Option[String]].right
    } yield
      ErrorSource(
          pointer = pointer,
          parameter = parameter
      )
  })

  implicit val errorDecoder = Decoder.instance[Error](hcursor ⇒ {
    for {
      id ← hcursor.downField(FieldNames.`id`).as[Option[String]].right
      status ← hcursor.downField(FieldNames.`status`).as[Option[String]].right
      code ← hcursor.downField(FieldNames.`code`).as[Option[String]].right
      title ← hcursor.downField(FieldNames.`title`).as[Option[String]].right
      detail ← hcursor.downField(FieldNames.`detail`).as[Option[String]].right
      links ← hcursor.downField(FieldNames.`links`).as[Option[Links]].right
      meta ← hcursor.downField(FieldNames.`meta`).as[Option[Meta]].right
      source ← hcursor.downField(FieldNames.`source`).as[Option[ErrorSource]].right
    } yield
      Error(
          id = id,
          status = status,
          code = code,
          title = title,
          detail = detail,
          links = links,
          meta = meta,
          source = source
      )
  })

  implicit val resourceObjectDecoder = Decoder.instance[ResourceObject](hcursor ⇒ {
    for {
      id ← hcursor.downField(FieldNames.`id`).as[Option[String]].right
      `type` ← hcursor.downField(FieldNames.`type`).as[String].right
      attributes ← hcursor.downField(FieldNames.`attributes`).as[Option[Attributes]].right
      relationships ← hcursor.downField(FieldNames.`relationships`).as[Option[Relationships]].right
      links ← hcursor.downField(FieldNames.`links`).as[Option[Links]].right
      meta ← hcursor.downField(FieldNames.`meta`).as[Option[Meta]].right
    } yield
      ResourceObject(
          id = id,
          `type` = `type`,
          attributes = attributes,
          relationships = relationships,
          links = links,
          meta = meta
      )
  })

  implicit val resourceObjectsDecoder =
    Decoder.instance[ResourceObjects](_.as[List[ResourceObject]].right.map(ResourceObjects))

  implicit val includedDecoder = Decoder.instance[Included](_.as[ResourceObjects].right.map(Included.apply))

  implicit val rootObjectDecoder = Decoder.instance[RootObject](hcursor ⇒ {
    for {
      data ← hcursor.downField(FieldNames.`data`).as[Option[Data]].right
      links ← hcursor.downField(FieldNames.`links`).as[Option[Links]].right
      errors ← hcursor.downField(FieldNames.`errors`).as[Option[Errors]].right
      meta ← hcursor.downField(FieldNames.`meta`).as[Option[Meta]].right
      included ← hcursor.downField(FieldNames.`included`).as[Option[Included]].right
      jsonapi ← hcursor.downField(FieldNames.`jsonapi`).as[Option[JsonApi]].right
    } yield
      RootObject(
          data = data,
          links = links,
          errors = errors,
          meta = meta,
          included = included,
          jsonApi = jsonapi
      )
  })
}

object CirceJsonapiDecoders extends CirceJsonapiDecoders
