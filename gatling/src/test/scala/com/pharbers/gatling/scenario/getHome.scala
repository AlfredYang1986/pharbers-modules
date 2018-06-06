package com.pharbers.gatling.scenario

import com.pharbers.gatling.base.phHttpProtocol
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class getHome extends Simulation {
	import com.pharbers.gatling.base.phHeaders._
	import com.pharbers.gatling.base.phHttpProtocol._

	val httpProtocol = phHttpProtocol("http://192.168.100.176:9000")

	val scn = scenario("qi_gatling_test")
		.exec(http("home")
			.get("/")
			.headers(headers_base))
		.pause(3)

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
