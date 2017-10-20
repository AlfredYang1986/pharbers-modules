package com.pharbers.panel.pfizer.impl

import java.util.UUID

import com.pharbers.memory.pages.pageMemory
import com.pharbers.panel.pfizer.{panel_file_path, phPfizerHandleTrait}
import com.pharbers.panel.util.csv.phHandleCsvImpl
import com.pharbers.panel.util.excel.impl.phHandleExcelImpl
import com.pharbers.panel.util.excel.impl.phHandleExcelImpl._

import scala.collection.immutable.Map
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 17-9-7.
  */
class phPfizerHandleImpl(args: Map[String, List[String]]) extends phPfizerHandleTrait with panel_file_path {
    private val cpas = args.getOrElse("cpas", throw new Exception("no find CPAs arg"))
    private val gycxs = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg"))
    private val company = args.getOrElse("company", throw new Exception("no find company arg")).head
    private val user = args.getOrElse("user", throw new Exception("no find user arg")).head
    private val markets = makets.split(",").toList
    private val excelParser = phHandleExcelImpl()

    override def calcYM: JsValue = {
        val c0 = loadCPA
        val g0 = loadGYCX
        val filtered_c0 = distinctYM(c0)
        val filtered_g0 = distinctYM(g0)

        val resultYM = for( c <- filtered_c0; g <- filtered_g0
                            if c._1 == g._1 ) yield {
            (c._1, c._2 + g._2)
        }

        resultYM.size match {
            case 0 => toJson("0")
            case 1 => toJson(resultYM.head._1)
            case _ => toJson(resultYM.keys.toList.mkString("#"))
        }
    }

    def loadCPA: (String, List[String]) = {
        val setDefaultMap = getDefault
        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { m =>
            Some(m ++ Map("YM" -> (m("YEAR") + getMonth(m("MONTH")))))
        }
        val cache_file_local = base_path + company + cache_local + UUID.randomUUID.toString + ".cache"
        val cpa_title = cpas.map { c =>
            excelParser.readExcelToCache(ExcelData(c, defaultValueArg = setDefaultMap), cache_file_local)
        }.distinct.flatten
        (cache_file_local, cpa_title)
    }

    def loadGYCX: (String, List[String]) = {
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
        val setDefaultMap = getDefault
        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { m =>
            Some(m ++ Map("YM" -> (m("YEAR") + getMonth(m("MONTH")))))
        }
        val cache_file_local = base_path + company + cache_local + UUID.randomUUID.toString + ".cache"
        val gycx_title = gycxs.map { g =>
            excelParser.readExcelToCache(ExcelData(g, defaultValueArg = setDefaultMap, fieldArg = setFieldMap), cache_file_local)
        }.distinct.flatten
        (cache_file_local, gycx_title)
    }

    private def getMonth(m: String): String = {
        m.toInt match {
            case i: Int if i < 9 => "0" + m
            case _: Int => m
        }
    }

    private def getDefault = Map(
        "PRODUCT_NAME" -> "$MOLE_NAME",
        "VALUE" -> "0",
        "STANDARD_UNIT" -> "0"
    )

    def distinctYM(arg: (String,List[String])): Map[String, Int] = {
        val page = pageMemory(arg._1)
        println(s"t.pageCount = ${page.pageCount}")

        val lst = (for(i <- 0 until page.pageCount.toInt) yield {
            page.pageData(i).map{ line =>
                val data = arg._2.zip(line.split(spl).toList).toMap
                Map("HOSPITAL_CODE" -> data("HOSPITAL_CODE"),"YM" -> data("YM"))
            }.groupBy(_("YM")).map { l =>
                l._1 -> l._2.distinct.length
            }.toList
        }).flatten
        val grouped = lst.groupBy(_._1).map { z =>
            z._1 -> z._2.map(_._2).sum
        }

        val maxYM = grouped.maxBy(x => x._2)
        grouped.filter(_._2 > maxYM._2/2)
    }

    override def getPanelFile(ym: List[String]): JsValue = {
        implicit val arg1 = Map("c0" -> loadCPA, "g0" -> loadGYCX)
        implicit val arg2 = Map("m1" -> load_m1, "hos00" -> load_hos00)

        val a = ym.map { x =>
            Map(x -> generatePanelFile(x))
        }
        a

        toJson(ym.map(x => (x, "ucData(x)")).toMap)
    }

    private def generatePanelFile(ym: String)
                                 (implicit arg1: Map[String,(String, List[String])],
                                  arg2: Map[String,List[Map[String,String]]]) = {
        val c0 = arg1.getOrElse("c0", throw new Exception("参数错误"))
        val g0 = arg1.getOrElse("g0", throw new Exception("参数错误"))
        val m1 = arg2.getOrElse("m1", throw new Exception("参数错误"))
        val hos00 = arg2.getOrElse("hos00", throw new Exception("参数错误"))

        markets.map { market =>
            val b0 = load_b0(market)
            val m1_c = innerJoin(b0.toStream, m1.toStream, "CPA反馈通用名", "通用名").map(mergeMB(_))
            val m1_g = innerJoin(b0.toStream, m1.toStream, "GYCX反馈通用名", "通用名").map(mergeMB(_))
            val hosp_tab = getHospTab(hos00, market)
            val hos0_hosp_id = hosp_tab.keys.toList

            def filter_source(source: (String, List[String]),
                              m1Arg: Stream[Map[String, String]],
                              append_local: String = ""): String = {
                implicit val panel_file_local: String = if (append_local == "") base_path + company + output_local + UUID.randomUUID.toString
                                        else append_local
                implicit val titleSeq: List[String] = "ID" :: "Hosp_name" :: "Date" :: "Prod_Name" :: "Prod_CNAME" ::
                                "HOSP_ID" :: "Strength" :: "DOI" :: "DOIE" :: "Units" :: "Sales" :: Nil
                val page = pageMemory(source._1)

                (0 until page.pageCount.toInt) foreach { i =>
                    val temp = page.pageData(i).map { line =>
                        val data = source._2.zip(line.split(spl).toList).toMap
                        if (data("YM") == ym && hos0_hosp_id.contains(data("HOSPITAL_CODE")))
                            data ++ Map("min1" -> getMin1Fun(data))
                        else
                            Map[String, String]()
                    }.filter(_ != Map())

                    innerJoin(m1Arg, temp, "min1", "min1")
                            .map(mergeMC(_, market, hosp_tab)).toList
                            .filter(_("Sales") != "")
                            .foreach { x =>
                                phHandleCsvImpl().sortInsert(x, distinct_source, mergeSameLine)
                            }
                }
                panel_file_local
            }

            val panel_local = filter_source(g0, m1_g, filter_source(c0, m1_c))
            Map(market -> panel_local)
        }
    }

    def load_m1: List[Map[String, String]] ={
        val m1_file_local = base_path + company + product_vs_ims_file
        val completed = excelParser.readExcel(ExcelData(m1_file_local))
        completed.map{x =>
            Map("min1" -> x("min1"), "min1_标准" -> x("min1_标准"), "通用名" -> x("通用名"))
        }.distinct
    }

    def load_hos00: List[Map[String, String]] ={
        val hos0_file_local = base_path + company + universe_inf_file
        val setFieldMap = Map(
            "样本医院编码" -> "ID",
            "PHA医院名称" -> "HOSP_NAME",
            "PHA ID" -> "HOSP_ID",
            "市场" -> "DOI"
        )
        val filter: Map[String, String] => Boolean = { tr =>
            tr.get("If Panel_All") match {
                case None => false
                case Some(s) if s == "1" => true
                case _ => false
            }
        }
        excelParser.readExcel(ExcelData(hos0_file_local, fieldArg = setFieldMap))
                .filter(filter)
                .map { x =>
                    x ++ Map("DOIE" -> x("DOI"))
                }
    }

    def load_b0(market: String) = {
        val b0_file_local = base_path + company + markets_file
        excelParser.readExcel(ExcelData(b0_file_local,sheetName = market))
    }

    private def getHospTab(hos00: List[Map[String,String]], market: String) = {
        val grouped = hos00.filter(_("DOI") == market + " Market").groupBy(_("ID"))
        grouped.flatMap{x =>
            Map(x._1 -> (x._2.head("HOSP_NAME"),x._2.head("HOSP_ID")))
        }
    }

    private val mergeMB:Map[String,String] => Map[String,String] = { old =>
        Map(
            "min1" -> old("min1"),
            "min1_标准" -> old("min1_标准"),
            "marketname" -> old("TA")
        )
    }

    private def getMin1Fun(tr: Map[String, String]): String = {
        tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME")
    }

    def innerJoin(lst1: Stream[Map[String, String]], lst2: Stream[Map[String, String]],
                          nameBylst1: String, nameBylst2: String): Stream[Map[String, String]] = {
        for(
            r1 <- lst1; r2 <- lst2
            if r1(nameBylst1) == r2(nameBylst2)
        )yield{
            r1 ++ r2
        }
    }

    private val mergeMC:(Map[String,String], String, Map[String,(String,String)]) => Map[String,Any] = { (old,market,hosId) =>
        Map(
            "ID" -> old("HOSPITAL_CODE").toLong,
            "Hosp_name" -> hosId(old("HOSPITAL_CODE"))._1,
            "Date" ->  old("YM"),
            "Prod_Name" -> old("min1_标准"),
            "Prod_CNAME" -> old("min1_标准"),
            "HOSP_ID" -> hosId(old("HOSPITAL_CODE"))._2,
            "Strength" -> old("min1_标准"),
            "DOI" -> market,
            "DOIE" -> market,
            "Units" -> old("STANDARD_UNIT").toDouble,
            "Sales" -> old("VALUE").toDouble
        )
    }

    private val distinct_source: (Map[String, Any], Map[String, Any]) => Int = { (newLine, cur) =>
        def getString(m: Map[String, Any]): String ={
            m("ID").toString + m("Hosp_name") + m("Date") + m("Prod_Name") + m("Prod_CNAME") + m("HOSP_ID") + m("Strength") + m("DOI") + m("DOIE")
        }

        if(cur == "") -1
        else if (getString(newLine) == getString(cur)) 0
        else if (getString(newLine) < getString(cur)) -1
        else 1
    }

    private val mergeSameLine: List[Map[String, Any]] => (Map[String, Any], Map[String, Any]) = { lst =>
        if(lst.length == 2){
            (lst.head ++ Map(
                "Units" -> (lst.head("Units").toString.toDouble + lst.last("Units").toString.toDouble),
                "Sales" -> (lst.head("Sales").toString.toDouble + lst.last("Sales").toString.toDouble)
            ), Map())
        }else {
            (lst.head, lst.last)
        }
    }
}
