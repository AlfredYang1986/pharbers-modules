package com.pharbers.channel.syncchannel.syncHelper

import play.api.libs.json.JsValue
import com.pharbers.bmmessages.CommonMessage
import com.pharbers.bmpattern.ModuleTrait

abstract class msg_syncCommand extends CommonMessage(cat ="sync test")

object syncMessage {
    case class msg_syncCallback(data : JsValue)(override val mt: ModuleTrait) extends msg_syncCommand
}
