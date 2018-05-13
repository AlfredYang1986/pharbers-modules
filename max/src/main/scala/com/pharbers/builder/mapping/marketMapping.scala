package com.pharbers.builder.mapping

trait marketMapping {

    def getMarketLst(company: String): List[String] = company match {
        case "testGroup" => "麻醉市场" :: Nil
        case "Astellas" => "麻醉市场1" :: "麻醉市场2" :: Nil
        case _ => ???
    }
}
