package com.pharbers.builder.mapping

trait universeMapping {
    def getUniverse(c: String, m: String): String = {
        (c, m) match {
            case ("testGroup", "麻醉市场") => "nhwa/universe_麻醉市场_online.xlsx"
            case (_, _) => ???
        }
    }
}
