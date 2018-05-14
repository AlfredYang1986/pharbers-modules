package com.pharbers.search

import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity

/**
  * Created by jeorch on 18-5-14.
  */
case class phMaxResultInfo(user: String, company: String, ym:String, mkt: String) {

    private val rd = new PhRedisDriver()
    private val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)

}
