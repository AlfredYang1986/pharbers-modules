package com.pharbers.panel.common

import org.bson.types.ObjectId
import scala.collection.immutable.Map
import com.pharbers.pactions.actionbase._
import com.pharbers.builder.phMarketTable.MongoDBPool._
import com.mongodb.casbah.Imports.{DBObject, MongoDBObject}
import com.pharbers.common.excel.{phExcelFileInfo, phHandleExcel}

object phBaseLine2MongoJob {
    def apply(args: pActionArgs): pActionTrait = new phBaseLine2MongoJob(args)
}

class phBaseLine2MongoJob(override val defaultArgs: pActionArgs) extends pActionTrait {

    override val name: String = "result"
    override def perform(pr : pActionArgs): pActionArgs = {
        val db = MongoPool.queryDBInstance("market").get
        val coll_name = "BaseLine"
        val file = defaultArgs.asInstanceOf[MapArgs].get("file").asInstanceOf[StringArgs].get
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get

        val lst = phHandleExcel().read2Lst(phExcelFileInfo(file))
        val marketLst = lst.map(_("MARKET")).distinct

        def m2d(map: Map[String, String], company: String, mkt: String): DBObject = {

            val builder = MongoDBObject.newBuilder
            builder += "_id" -> ObjectId.get()      // user_id 唯一标示
            builder += "Company" -> company
            builder += "Market" -> mkt
            builder += "Date" -> map("DATE").toInt
            builder += "Sales" -> map("PANEL_SALES").toDouble
            builder += "HOSP_ID" -> map("PANEL_HOSP_COUNT").toDouble
            builder += "Prod_Name" -> map("PANEL_PROD_COUNT").toDouble
            builder.result()
        }

        marketLst.foreach { mkt =>
            val mktMap = lst.filter(_("MARKET") == mkt)
            if(mktMap.length != 12) throw new Exception(s"$mkt data size not is 12")

            val condition: DBObject = {
                DBObject("Company" -> company)
                DBObject("Market" -> mkt)
            }
            db.deleteMultiObject(condition, coll_name)

            mktMap.foreach(insertData =>
                db.insertObject(m2d(insertData, company, mkt), coll_name, "_id")
            )
        }

        NULLArgs
    }
}