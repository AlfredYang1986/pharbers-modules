package com.pharbers.gatling.base

object phHeaders {

    val headers_base = Map(
        "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "Upgrade-Insecure-Requests" -> "1")

    val headers_0 = Map(
        "Content-Type" -> "application/x-www-form-urlencoded; charset=UTF-8",
        "Origin" -> "http://192.168.100.254",
        "X-Requested-With" -> "XMLHttpRequest")

    val headers_1 = Map(
        "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "Upgrade-Insecure-Requests" -> "1")

    val headers_2 = Map(
        "Content-Type" -> "application/json,charset=utf-8",
        "Origin" -> "http://192.168.100.176:9000",
        "X-Requested-With" -> "XMLHttpRequest")

    val headers_3 = Map(
        "Accept" -> "*/*",
        "Content-Type" -> "text/plain;charset=UTF-8",
        "Origin" -> "http://192.168.100.176:9000")



    val uri1 = "http://a1.easemob.com/1111170725178064/maxloading/token"
    val uri3 = "http://192.168.100.254/cgi-bin/luci/;stok=e3ea99128c07142034023a0ab3de3b9d/apmngr_status"

}
