package com.pharbers.common.xmpp.em

/**
  * Created by spark on 18-4-26.
  */
trait EmBase {

    lazy val host: String = "https://a1.easemob.com"
    lazy val org_name: String = "1111170725178064"
    lazy val app_name: String = "maxloading"

    lazy val client_id: String = "YXA6Rn3RUEh-EeiN1jkcocvaLg"
    lazy val client_secret: String = "YXA60QDo-kf6WO_EkGhOFtU74Kq3zAA"

    val emUrl: String = host + "/" + org_name + "/" + app_name + "/"

}
