package com.pharbers.pfizer.impl

import GeneratePanel._
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
import com.pharbers.baseModules.PharbersInjectModule

import scala.concurrent.duration._

/**
  * Created by clock on 17-9-7.
  */
class phPfizerHandleImpl(implicit as:ActorSystem) extends phPfizerHandleTrait {
    override def generatePanelFile(args: Map[String, List[String]]): JsValue = {
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
}

class GeneratePanel extends Actor with ActorLogging with PharbersInjectModule {
    override val id: String = "data-parse"
    override val configPath: String = "pharbers_config/data_parse_file.xml"
    override val md = "contrast_file_path" :: "product_vs_ims_file" :: "universe_inf_file" :: "markets_file" :: "phaid_file" :: "output_file" :: Nil

    val path : String = config.mc.find(p => p._1 == "contrast_file_path").get._2.toString
    val product_vs_ims_file : String = config.mc.find(p => p._1 == "product_vs_ims_file").get._2.toString
    val universe_inf_file : String = config.mc.find(p => p._1 == "universe_inf_file").get._2.toString
    val markets_file : String = config.mc.find(p => p._1 == "markets_file").get._2.toString
    val PHAID_file : String = config.mc.find(p => p._1 == "phaid_file").get._2.toString
    val output_file : String = config.mc.find(p => p._1 == "output_file").get._2.toString

    override def receive: Receive = {
        case StartGeneratePanel(args) => startGeneratePanel(args)
        case ReadCPAInDB(cpas, collectionName) => readCPAInDB(cpas, "1", collectionName)
        case ReadGYCXInDB(gycxs, collectionName) => readGYCXInDB(gycxs, "1", collectionName)
        case _ => throw new Exception("generate panel file error")
    }

    private val parser: phHandleExcelTrait = new phHandleExcelImpl

    var c0: List[Map[String,String]] = Nil
    var g0: List[Map[String,String]] = Nil
    var m1: List[Map[String,String]] = Nil
    var hos00: List[Map[String,String]] = Nil
    var b0: List[Map[String,String]] = Nil
    var ID_2_PHAID: Map[String,(String,String)] = Map()

    var panel: List[Map[String, Any]] = Nil

    private def startGeneratePanel(args: Map[String, List[String]]) = {
        val company_name = args.getOrElse("company", throw new Exception("no find company arg")).head
        val cpas = args.getOrElse("cpas", throw new Exception("no find cpas arg"))
        val gycxs = args.getOrElse("gycxs", throw new Exception("no find gycxs arg"))
        val ym = args.getOrElse("ym", throw new Exception("no find ym arg")).head
        val out_put_local = args.getOrElse("out_put", path + output_file :: Nil).head

        //println(s"---------开始生成panel文件：$getDate---------------")
        //println()
        //println(s"---------处理的公司名：$company_name---------------")
        //println(s"---------要处理的CPA文件有：$cpas-------------------")
        //println(s"---------要处理的GYCX文件有：$gycxs-----------------")
        //println()

        //println("1. 导入数据到db(包括2.修改列名 3.月份筛选 4.填补缺失值 5.生成最小产品单位)")
        //println()
        readCPAInDB(cpas,ym)
        readGYCXInDB(gycxs,ym)

        //println("6.1 导入PVI others文件")
        val product_vs_ims_file_local = path + product_vs_ims_file
        readProductVSImsInDB(product_vs_ims_file_local)

        //println("6.2 导入universe_inf文件")
        val universe_inf_file_local = path + universe_inf_file
        readUniverseInDB(universe_inf_file_local)

        //println("7 生成panel")
        val markets_file_local = path + markets_file
        val market = List("INF")
        generatePanel(markets_file_local, market)

        writePanel(out_put_local)

        sender() ! toJson("panel file generate complete")
    }


    private def readCPAInDB(files: List[String], ym: String, collectionName: String = "c0") = {
        //println()
        //println(s"读入 CPA 数据 到数据库 $getDate")
        val startTime = new Date()

        //3.月份筛选
        implicit val filterFun = getYMFilter(_: Map[String, String], ym)

        //4.填补缺失值
        val setDefaultMap = getDefault

        //5. 生成最小产品单位
        implicit val postFun = getGenerateMin1Fun

//        _data_connection.getCollection(collectionName).drop
        files.foreach{file =>
            c0 = c0 ++ parser.readToDB(file, collectionName, defaultValueArg = setDefaultMap)
        }

        //println(s"读入 CPA 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        //println()
    }

    private def readGYCXInDB(files: List[String], ym: String, collectionName: String = "g0") = {
        //println()
        //println(s"读入 GYCX 数据 到数据库 $getDate")
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
        implicit val postFun = getGenerateMin1Fun

//        _data_connection.getCollection(collectionName).drop
        files.foreach{file =>
            g0 = g0 ++ parser.readToDB(file, collectionName, fieldArg = setFieldMap, defaultValueArg = setDefaultMap)
        }

        //println(s"读入 GYCX 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        //println()
    }

    private def readProductVSImsInDB(file: String, collectionName: String = "m1") = {
        //println()
        //println(s"读入 产品标准化 vs IMS_Pfizer_6市场others 数据 到数据库 $getDate")
        val startTime = new Date()
//        val coll = _data_connection.getCollection(collectionName)
//        coll.drop

        val result = parser.readToList(file).map{x =>
            Map("min1" -> x("min1"), "min1_标准" -> x("min1_标准"), "通用名" -> x("通用名"))
        }

        m1 = result.distinct

//        result.foreach{x =>
//            val builder = MongoDBObject.newBuilder
//            x.keys.foreach{k =>
//                builder += k -> x(k)
//            }
//            coll += builder.result()
//        }

        //println(s"读入 产品标准化 vs IMS_Pfizer_6市场others 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        //println()
    }

    private def readUniverseInDB(file: String, collectionName: String = "hos00") = {
        //println()
        //println(s"读入 universe_inf 数据 到数据库 $getDate")
        val startTime = new Date()
        //列名修改
        val setFieldMap = Map(
            "样本医院编码" -> "ID",
            "PHA医院名称" -> "HOSP_NAME",
            "PHA ID" -> "HOSP_ID",
            "市场" -> "DOI"
        )

        //筛选
        implicit val filterFun: Map[String, String] => Boolean = {tr =>
            tr.get("If Panel_All") match {
                case None => false
                case Some(s) if s == "1" => true
                case _ => false
            }
        }

        //新建列
        implicit val postFun: (Map[String,String]) => Option[Map[String, String]] = {tr =>
           Some(tr ++ Map("DOIE" -> tr("DOI")))
        }

//        _data_connection.getCollection(collectionName).drop
        hos00 = parser.readToDB(file, collectionName, fieldArg = setFieldMap)

        //println(s"读入 universe_inf 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
        //println()
    }

    private def generatePanel(file: String, marketLst: List[String], collectionName: String = "b0") = {
        import com.pharbers.util.excel.impl.phHandleExcelImpl._

        //println()
        //println(s"读入 辉瑞采购清单中的通用名划分的6个市场 数据 到数据库 $getDate")

//        _data_connection.getCollection(collectionName).drop
        //println("7.1 读入others中INF市场到数据库 =>  b0")
        marketLst.foreach{market =>
            b0 = b0 ++ parser.readToDB(file, collectionName, sheetName = market)
            panel = panel ++ innerJoin(market)
        }

        //println()
    }

    private def innerJoin(market: String) = {
        //println(s"7.2 对b0 和 m1 进行 inner join $getDate")
        val hos0_hosp_name = hos00.filter(_("DOI") == market+" Market").map(_("ID"))

        var m1_c: List[Map[String, String]] = Nil
        var m1_g: List[Map[String, String]] = Nil

        def mergeMap1(m: Map[String,String], b: Map[String,String]): Map[String,String] ={
            Map(
                "min1" -> m("min1"),
                "min1_标准" -> m("min1_标准"),
                "marketname" -> b("TA")
            )
        }
        def mergeMap2(ag: Map[String,String], m: Map[String,String]): Map[String,Any] ={
            val M: String = if(ag("MONTH").toInt < 9) "0" + ag("MONTH") else ag("MONTH")
            val YM: String = ag("YEAR") + M

            Map(
                "ID" -> ag("HOSPITAL_CODE").toLong,
                "Hosp_name" -> ID_2_PHAID(ag("HOSPITAL_CODE"))._2,
                "Date" ->  YM,
                "Prod_Name" -> m("min1_标准"),
                "Prod_CNAME" -> m("min1_标准"),
                "HOSP_ID" -> ID_2_PHAID(ag("HOSPITAL_CODE"))._1,
                "Strength" -> m("min1_标准"),
                "DOI" -> market,
                "DOIE" -> market,
                "Units" -> ag("STANDARD_UNIT").toDouble,
                "Sales" -> ag("VALUE").toDouble
            )
        }
//        def saveToDB(row: Map[String, Any]) = {
//            val builder = MongoDBObject.newBuilder
//            row.keys.foreach{k =>
//                builder += k -> row(k)
//            }
//            builder.result()
//        }

//        val coll_m1_c = _data_connection.getCollection("m1_c")
//        val coll_m1_g = _data_connection.getCollection("m1_g")
//        coll_m1_c.drop
//        coll_m1_g.drop
        m1.foreach { m =>
            b0.foreach { b =>
                if (m("通用名") == b("CPA反馈通用名")) {
                    val row = mergeMap1(m, b)
//                    coll_m1_c += saveToDB(row)
                    m1_c = m1_c :+ row
                }
            }
            b0.foreach { b =>
                if (m("通用名") == b("GYCX反馈通用名")) {
                    val row = mergeMap1(m, b)
//                    coll_m1_g += saveToDB(row)
                    m1_g = m1_g :+ row
                }
            }
        }

        ID_2_PHAID = get_ID_to_PHAID_map

        //println(s"7.3 7.4 7.5 生成t1表  $getDate")
//        val coll_panel = _data_connection.getCollection("panel")
        var t1: List[Map[String, Any]] = Nil
//        coll_panel.drop

        c0.filter(x => hos0_hosp_name.contains(x("HOSPITAL_CODE"))).foreach { c =>
            m1_c.foreach { mc =>
                if (c("min1").trim == mc("min1").trim) {
                    val row = mergeMap2(c, mc)
//                    coll_panel += saveToDB(row)
                    t1 = t1 :+ row
                }
            }
        }
        g0.filter(x => hos0_hosp_name.contains(x("HOSPITAL_CODE"))).foreach { g =>
            m1_g.foreach { mg =>
                if (g("min1").trim == mg("min1").trim) {
                    val row = mergeMap2(g, mg)
//                    coll_panel += saveToDB(row)
                    t1 = t1 :+ row
                }
            }
        }

        val t1_f = t1.filter(_ ("Sales") != "")
        val t1_g = t1_f.groupBy(x => x("ID").toString + x("Hosp_name") + x("Date") + x("Prod_Name") + x("Prod_CNAME") + x("HOSP_ID") + x("Strength") + x("DOI") + x("DOIE"))
        val t1_s = t1_g.map{x =>
            x._2.head ++ Map(
                "Units" -> x._2.map(_("Units").asInstanceOf[Double]).sum,
                "Sales" -> x._2.map(_("Sales").asInstanceOf[Double]).sum
            )
        }
        t1_s
    }

    private def get_ID_to_PHAID_map: Map[String, (String,String)] = {
        val PHAID_file_local = path + PHAID_file
        val result = parser.readToList(PHAID_file_local).map{x =>
            x("Panel ID") -> (x("PHA ID"), x("PHA Hosp name"))
        }.toMap
        result
    }

    def writePanel(out_put_local: String) = {
        val writeSeq = Map(
                "ID" -> 0,
                "Hosp_name" -> 1,
                "Date" -> 2,
                "Prod_Name" -> 3,
                "Prod_CNAME" -> 4,
                "HOSP_ID" -> 5,
                "Strength" -> 6,
                "DOI" -> 7,
                "DOIE" -> 8,
                "Units" -> 9,
                "Sales" -> 10
        )

        parser.writeByList(out_put_local, panel, writeSeq)
    }

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
                case Some(s) if "0" + s == month => true
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

    private val getGenerateMin1Fun: (Map[String,String]) => Option[Map[String, String]] = {tr =>
        Some(tr ++ Map("min1" -> (tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME"))))
    }
}
