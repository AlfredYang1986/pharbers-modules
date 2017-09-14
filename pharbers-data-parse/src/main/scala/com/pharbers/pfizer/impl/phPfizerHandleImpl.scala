package com.pharbers.pfizer.impl

import GeneratePanel._
import com.pharbers.util.excel.impl.phHandleExcelImpl._
import com.pharbers.mongodbConnect._data_connection
import com.pharbers.pfizer.phPfizerHandleTrait
import com.pharbers.util.excel.impl.phHandleExcelImpl
import com.pharbers.util.excel.phHandleExcelTrait
import java.text.SimpleDateFormat
import java.util.Date

import scala.collection.immutable.Map
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import com.mongodb.DBObject
import com.mongodb.casbah.Imports.MongoDBObject
import com.mongodb.casbah.commons.Imports

import scala.collection.mutable
import scala.concurrent.duration._


/**
  * Created by clock on 17-9-7.
  */
class phPfizerHandleImpl(implicit as:ActorSystem) extends phPfizerHandleTrait {
    override def generatePanelFile(args: Map[String, List[String]]): JsValue = {
        //1.读入CPA/GYCX 数据到DB  => c0 g0
        implicit val timeout = Timeout(15 minutes)

        val actor = as.actorOf(GeneratePanel.props)
        val future = actor ? StartGeneratePanel(args)

        Await.result(future, timeout.duration).asInstanceOf[JsValue]
    }
}

object GeneratePanel {
    def props = Props(new GeneratePanel)

    case class StartGeneratePanel(args: Map[String, List[String]])
    case class ReadCPAInDB(cpas: List[String], collectionName: String)
    case class ReadCPAComplete()
    case class ReadGYCXInDB(gycxs: List[String], collectionName: String )
    case class ReadGYCXComplete()

    private def getDate = {
        val currentTime = new Date()
        val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        formatter.format(currentTime)
    }

    private def getConsumingTime(start: Date): String ={
        val consumingTime = new Date(new Date().getTime - start.getTime)
        val formatter = new SimpleDateFormat("mm:ss")
        formatter.format(consumingTime)
    }

    private val getYMFilter: (Map[String, String], String) => Boolean = { (tr, ym) =>
        val year = ym.substring(0, 4)
        val month = ym.substring(4)
        val fy = {
            tr.get("YEAR") match {
                case None => false
                case Some(s) if s == year => true
                case _ => false
            }
        }
        val fm = {
            tr.get("MONTH") match {
                case None => false
                case Some(s) if s == month => true
                case _ => false
            }
        }

        if(fy && fm) true else false
    }

    private def getDefault = Map(
        "PRODUCT_NAME" -> "$MOLE_NAME",
        "VALUE" -> "0",
        "STANDARD_UNIT" -> "0"
    )

    private val getPostFun: mutable.Builder[(String, Any),Imports.DBObject] => Unit = {builder =>
        val obj = builder.result()
        builder += "min1" -> (obj.get("PRODUCT_NAME").asInstanceOf[String] +
                obj.get("APP2_COD").asInstanceOf[String] +
                obj.get("PACK_DES").asInstanceOf[String] +
                obj.get("PACK_NUMBER").asInstanceOf[String] +
                obj.get("CORP_NAME").asInstanceOf[String])
    }
}

class GeneratePanel extends Actor with ActorLogging{
    private val parser: phHandleExcelTrait = new phHandleExcelImpl

    override def receive: Receive = {
        case StartGeneratePanel(args) => startGeneratePanel(args)
        case ReadCPAInDB(cpas, collectionName) => readCPAInDB(cpas, "1", collectionName)
        case ReadGYCXInDB(gycxs, collectionName) => readGYCXInDB(gycxs, "1", collectionName)
        case _ => throw new Exception("generate panel file error")
    }

    private def startGeneratePanel(args: Map[String, List[String]]) = {
        val company_name = args.getOrElse("company", throw new Exception("no find company arg")).headOption.get
        val cpas = args.getOrElse("cpas", throw new Exception("no find cpas arg"))
        val gycxs = args.getOrElse("gycxs", throw new Exception("no find gycxs arg"))
        val ym = args.getOrElse("ym", throw new Exception("no find ym arg")).headOption.get

        println(s"---------开始生成panel文件：$getDate---------------")
        println()
        println(s"---------处理的公司名：$company_name---------------")
        println(s"---------要处理的CPA文件有：$cpas-------------------")
        println(s"---------要处理的GYCX文件有：$gycxs-----------------")
        println()

        println("1. 导入数据到db(包括2.修改列名 3.月份筛选 4.填补缺失值 5.生成最小产品单位)")
        println()
//        readCPAInDB(cpas,ym)
//        readGYCXInDB(gycxs,ym)

        println("6.1 导入PVI others文件")
//        val product_vs_ims_file_local = "/home/clock/Downloads/产品标准化+vs+IMS_Pfizer_6市场others.xlsx"
//        readProductVSImsInDB(product_vs_ims_file_local)

        println("6.2 导入universe_inf文件")
//        val universe_inf_file_local = "/home/clock/Downloads/universe_inf.xlsx"
//        readUniverseInDB(universe_inf_file_local)

        println("7.1 导入INF市场文件")
        val markets_file_local = "/home/clock/Downloads/按辉瑞采购清单中的通用名划分6市场others.xlsx"
        val market = List("INF")
        readMarketInDB(markets_file_local, market)





        sender() ! toJson("Fucking trouble")
    }


    private def readCPAInDB(files: List[String], ym: String, collectionName: String = "c0") = {
        println()
        println(s"读入 CPA 数据 到数据库 $getDate")
        val startTime = new Date()

        //3.月份筛选
        implicit val filterFun = getYMFilter(_: Map[String, String], ym)

        //4.填补缺失值
        val setDefaultMap = getDefault

        //5. 生成最小产品单位
        implicit val postFun = getPostFun

        _data_connection.getCollection(collectionName).drop
        files.foreach{file =>
            parser.readToDB(file, collectionName, defaultValueArg = setDefaultMap)
        }

        println(s"读入 CPA 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        println()
    }

    private def readGYCXInDB(files: List[String], ym: String, collectionName: String = "g0") = {
        println()
        println(s"读入 GYCX 数据 到数据库 $getDate")
        val startTime = new Date()
        //2.列名修改
        val setFieldMap = Map(
            "城市" -> "CITY",
            "年" -> "YEAR",
            "月" -> "MONTH",
            "医院编码" -> "HOSPITAL_CODE",
            "通用名" -> "MOLE_NAME",
            "药品商品名" -> "PRODUCT_NAME",
            "规格" -> "PACK_DES",
            "包装规格" -> "PACK_NUMBER",
            "金额" -> "VALUE",
            "最小制剂单位数量" -> "STANDARD_UNIT",
            "剂型" -> "APP2_COD",
            "给药途径" -> "APP1_COD",
            "生产企业" -> "CORP_NAME"
        )

        //3.月份筛选
        implicit val filterFun = getYMFilter(_: Map[String, String], ym)

        //4.填补缺失值
        val setDefaultMap = getDefault

        //5. 生成最小产品单位
        implicit val postFun = getPostFun

        _data_connection.getCollection(collectionName).drop
        files.foreach{file =>
            parser.readToDB(file, collectionName, fieldArg = setFieldMap, defaultValueArg = setDefaultMap)
        }

        println(s"读入 GYCX 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        println()
    }

    private def readProductVSImsInDB(file: String, collectionName: String = "m1") = {
        println()
        println(s"读入 产品标准化 vs IMS_Pfizer_6市场others 数据 到数据库 $getDate")
        val startTime = new Date()
        val coll = _data_connection.getCollection(collectionName)
        coll.drop

        val result = parser.readToList(file).map{x =>
            val builder = MongoDBObject.newBuilder
            builder += "min1" -> x("min1")
            builder += "min1_标准" -> x("min1_标准")
            builder += "通用名" -> x("通用名")
            builder.result()
        }.distinct

        result.foreach(coll += _)

        println(s"读入 产品标准化 vs IMS_Pfizer_6市场others 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        println()
    }

    private def readUniverseInDB(file: String, collectionName: String = "hos00") = {
        println()
        println(s"读入 universe_inf 数据 到数据库 $getDate")
        val startTime = new Date()
        //2.列名修改
        val setFieldMap = Map(
            "样本医院编码" -> "ID",
            "PHA医院名称" -> "HOSP_NAME",
            "PHA ID" -> "HOSP_ID",
            "市场" -> "DOI"
        )

        //3.筛选
        implicit val filterFun: Map[String, String] => Boolean = {tr =>
            tr.get("If Panel_All") match {
                case None => false
                case Some(s) if s == "1" => true
                case _ => false
            }
        }

        //5. 生成最小产品单位
        implicit val postFun: mutable.Builder[(String, Any),Imports.DBObject] => Unit = {builder =>
            val obj = builder.result()
            builder += "DOIE" -> obj.get("DOI").asInstanceOf[String]
        }

        _data_connection.getCollection(collectionName).drop
        parser.readToDB(file, collectionName, fieldArg = setFieldMap)

        println(s"读入 universe_inf 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        println()
    }

    private def readMarketInDB(file: String, marketLst: List[String], collectionName: String = "b0") = {
        println()
        println(s"读入 辉瑞采购清单中的通用名划分的6个市场 数据 到数据库 $getDate")
        val startTime = new Date()

        _data_connection.getCollection(collectionName).drop
        marketLst.foreach{market =>
            parser.readToDB(file, collectionName, sheetName = market)
        }

        println(s"读入 辉瑞采购清单中的通用名划分的6个市场 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        println()
    }

}
