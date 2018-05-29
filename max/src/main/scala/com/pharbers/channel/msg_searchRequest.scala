package com.pharbers.channel

import com.pharbers.bmmessages.CommonMessage
import play.api.libs.json.JsValue

/**
  * Created by spark on 18-4-24.
  */
class msg_searchRequest extends CommonMessage("searchRequest", searchRequestModule)

object searchRequestMessage {
    case class msg_search(jv: JsValue) extends msg_searchRequest
    case class msg_searchResponse(jv: JsValue) extends msg_searchRequest
}
