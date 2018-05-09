package com.pharbers.common.xmpp.em.emImpl

import play.api.libs.json.Json.toJson
import com.pharbers.http.{HTTP, httpOpt}

/**
  * Created by spark on 18-4-27.
  */
trait EmInstance extends EmBase {

    val adminToken: String = {
        val args = toJson(
            Map(
                "grant_type" -> "client_credentials",
                "client_id" -> client_id,
                "client_secret" -> client_secret
            )
        )

        val result = try {
            HTTP(emUrl + "token").header("Accept" -> "application/json", "Content-Type" -> "application/json").post(args)
        } catch {
            case _: java.net.UnknownHostException => throw new Exception("")
        }

        (result \ "access_token").asOpt[String].get
    }

    def proxy(api: String): httpOpt = {
        HTTP(emUrl + api).header("Accept" -> "application/json",
            "Content-Type" -> "application/json",
            "Authorization" -> ("Bearer " + adminToken)
        )
    }

}
