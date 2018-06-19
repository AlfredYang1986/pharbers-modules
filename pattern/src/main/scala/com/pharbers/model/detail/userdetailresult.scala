package com.pharbers.model.detail

import io.circe.generic.JsonCodec

@JsonCodec
case class userdetailresult (id : String,
                             major : java.lang.Integer,
                             minor : java.lang.Integer,
                             user : Option[user],
                             company : Option[company]) extends commonresult