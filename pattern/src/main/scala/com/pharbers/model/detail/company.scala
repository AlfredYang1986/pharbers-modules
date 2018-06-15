package com.pharbers.model.detail

import io.circe.generic.JsonCodec

@JsonCodec case class company (id : String,
                               company_name: String) extends commonresult
