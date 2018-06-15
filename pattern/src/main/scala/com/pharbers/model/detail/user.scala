package com.pharbers.model.detail

import io.circe.generic.JsonCodec

@JsonCodec case class user(id : String,
                           name: String,
                           photo : String,
                           another_test : java.lang.Double,
                           screen_name : String,
                           final_test : java.lang.Integer,
                           auth : List[String]) extends commonresult

