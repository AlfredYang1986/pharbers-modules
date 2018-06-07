package com.pharbers.gatling.simulation

import io.gatling.core.Predef._
import scala.concurrent.duration._

import com.pharbers.gatling.scenario.getHome
import com.pharbers.gatling.base.phHttpProtocol

class getHome extends Simulation {
	import com.pharbers.gatling.base.phHttpProtocol.{noneBlackList, noneWhiteList}

	val httpProtocol = phHttpProtocol("http://192.168.100.176:9000")

	val scn = scenario("max_home")
		.exec(getHome.getHome)
		.pause(20)

	setUp(scn.inject(rampUsers(100) over (10 seconds))).protocols(httpProtocol)
}
