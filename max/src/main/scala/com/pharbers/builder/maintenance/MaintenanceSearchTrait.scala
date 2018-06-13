package com.pharbers.builder.maintenance

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import com.pharbers.builder.phMarketTable.phMarketManager

/**
  * Created by jeorch on 18-6-6.
  */
trait MaintenanceSearchTrait extends phMarketManager {
    override def getAllCompanies: (Option[Map[String, JsValue]], Option[JsValue]) = {
        val a = super.getAllCompanies
        (Some(Map("companies" -> toJson(getMktTableAllCompanies))), None)
    }

    def getDataCleanModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = ???
//        getModuleArgs(jv)(getDataCleanArgs)

    def getSimpleModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = ???
//        getModuleArgs(jv)(getPanelArgs)

    def getMaxModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = ???
//        getModuleArgs(jv)(getMaxArgs)

    def getDeliveryModuleArgs(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = ???
//        getModuleArgs(jv)(getDeliveryArgs)

    def getModuleArgs(jv: JsValue)(func: (String, String) => Map[String, String]): (Option[Map[String, JsValue]], Option[JsValue]) = {
        val company_id = (jv \ "condition" \ "maintenance" \ "company_id").asOpt[String].get

//        val allArgs = getAllMkt(company_id).map(x => func(company_id, x) ++ Map("Market" -> x))
//
//        val matchFileLst: List[Map[String, String]] =
//            allArgs.reduce((m1, m2) => m1 ++ m2 - "universe_file" - "Market")
//                .map(x => Map("file_key" -> x._1, "file_name" -> x._2.split("/").last, "title" -> x._2.split("/").last.split(46.toChar).head)).toList
//
//        val universeFileLst: List[Map[String, String]] = allArgs match {
//            case lst if lst.reduce((m1, m2) => m1 ++ m2).keys.exists(x => x == "universe_file") =>
//                allArgs.map(x =>Map("file_key" -> "universe_file", "file_name" -> x("universe_file").split("/").last, "title" -> x("universe_file").split("/").last.split(46.toChar).head, "Market" -> x("Market")))
//            case _ => List.empty
//        }

//        (Some(Map("match_tables" -> toJson(matchFileLst), "universe_files" -> toJson(universeFileLst))), None)
        ???
    }






        def getCompanyTables(company: String): List[Map[String, String]] = ???
    //        marketTable.filter(x => company == x("company"))

        def getNotCompanyTables(company: String): List[Map[String, String]] = ???
    //        marketTable.filter(x => company != x("company"))

        def getTable(company: String, market: String): Map[String, String] = ???
    //        marketTable.find(x => company == x("company") && market == x("market")).getOrElse(throw new Exception("input wrong"))

        def getSourceLst(company: String, market: String): Array[String] =
            getTable(company, market).getOrElse("source", throw new Exception("input wrong")).split("#")

        def getDataCleanArgLst(company: String, market: String): Array[String] =
            getTable(company, market).getOrElse("dataCleanArgs", throw new Exception("input wrong")) match {
                case "" => Array.empty[String]
                case s => s.split("#")
            }


        def getPanelArgLst(company: String, market: String): Array[String] =
            getTable(company, market).getOrElse("panelArgs", throw new Exception("input wrong")).split("#")

        def getMaxArgLst(company: String, market: String): Array[String] =
            getTable(company, market).getOrElse("maxArgs", throw new Exception("input wrong")).split("#")

        def getDeliveryArgLst(company: String, market: String): Array[String] =
            getTable(company, market).getOrElse("deliveryArgs", throw new Exception("input wrong")).split("#")

        def getDataCleanArgs(company: String, market: String): Map[String, String] = {
            val table = getTable(company, market)
            getDataCleanArgLst(company, market) match {
                case lst if lst.isEmpty => Map.empty
                case lst if lst.nonEmpty => lst.map(x => x -> table(x)).toMap
            }
        }

        def getPanelArgs(company: String, market: String): Map[String, String] = {
            val table = getTable(company, market)
            getPanelArgLst(company, market).map(x => x -> table(x)).toMap
        }

        def getMaxArgs(company: String, market: String): Map[String, String] = {
            val table = getTable(company, market)
            getMaxArgLst(company, market).map(x => x -> table(x)).toMap
        }

        def getDeliveryArgs(company: String, market: String): Map[String, String] = {
            val table = getTable(company, market)
            getDeliveryArgLst(company, market).map(x => x -> table(x)).toMap
        }


}
