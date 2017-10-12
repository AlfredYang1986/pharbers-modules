package com.pharbers.pfizer.impl

import java.util.UUID

import com.pharbers.pfizer.impl.parseData.parseMap
import com.pharbers.pfizer.{panel_file_path, phPfizerHandleTrait}
import com.pharbers.util.excel.impl.phHandleExcelImpl
import com.pharbers.util.excel.impl.phHandleExcelImpl._

import scala.collection.immutable.Map
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 17-9-7.
  */
object parseData{
    var parseMap: Map[String, (List[Map[String,String]], List[Map[String,String]])] = Map()
}

class phPfizerHandleImpl(args: Map[String, List[String]]) extends phPfizerHandleTrait {
    private val file_config = panel_file_path()
    private val excelParser = phHandleExcelImpl()
    private val cpas = args.getOrElse("cpas", throw new Exception("no find CPAs arg"))
    private val gycxs = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg"))
    private val company = args.getOrElse("company", throw new Exception("no find company arg")).head
    private val user = args.getOrElse("user", throw new Exception("no find user arg")).head

    private val c0 = parseMap.get(company+user) match {
        case Some((c: List[Map[String,String]], _)) if c == Nil => loadCPA(cpas)
        case Some((c: List[Map[String,String]], _)) => c
        case None => loadCPA(cpas)
    }
    private val g0 = parseMap.get(company+user) match {
        case Some((_, g: List[Map[String,String]])) if g == Nil => loadGYCX(gycxs)
        case Some((_, g: List[Map[String,String]])) => g
        case None => loadGYCX(gycxs)
    }
    parseMap += company+user -> (c0,g0)

    private def loadCPA(cs: List[String]): List[Map[String,String]] = {
        val setDefaultMap = getDefault
        val completed = cs.flatMap(c => excelParser.readExcel(ExcelData(c, defaultValueArg = setDefaultMap)))
        val processed = completed.map { x =>
            x ++ Map("YM" -> (x("YEAR") + getMonth(x("MONTH"))))
        }
        processed
    }

    private def loadGYCX(gs: List[String]): List[Map[String, String]] = {
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
        val completed = gs.flatMap(g => excelParser.readExcel(ExcelData(g, defaultValueArg = setDefaultMap, fieldArg = setFieldMap)))
        val processed = completed.map { x =>
            x ++ Map("YM" -> (x("YEAR") + getMonth(x("MONTH"))))
        }
        processed
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

    override def calcYM: JsValue = {
        val filtered_c0 = distinctYM(c0)
        val filtered_g0 = distinctYM(g0)

        val result = for(
                    c <- filtered_g0;
                    g <- filtered_c0
                    if c._1 == g._1 ) yield {
                (c._1,c._2 + g._2)
        }

        result.size match {
            case 0 => toJson("0")
            case 1 => toJson(result.head._1)
            case _ => toJson(result.keys.toList.mkString("#"))
        }
    }

    private def distinctYM(lst: List[Map[String, String]]): Map[String, Int] ={
        val grouped = lst.groupBy(_("YM")).map{c =>
            c._1 -> c._2.map(r => "HOSPITAL_CODE" -> r("HOSPITAL_CODE")).distinct.length
        }

        val maxYM = grouped.maxBy(x => x._2)
        val filtered = grouped.filter(_._2 > maxYM._2/2)
        filtered
    }

    override def generatePanelFile(ym: String): JsValue = {
        val filter_ym_c0 = filterSource(c0,ym)
        val filter_ym_g0 = filterSource(g0,ym)
        val m1 = load_m1
        val hos00 = load_hos00
        val markets = List("INF")
        val panel = markets.flatMap{ market =>
            val b0 = load_b0(market)
            val m1_c = innerJoin(b0,m1,"CPA反馈通用名","通用名").map(mergeMB(_))
            val m1_g = innerJoin(b0,m1,"GYCX反馈通用名","通用名").map(mergeMB(_))
            val hosp_tab = getHospTab(hos00, market)
            val hos0_hosp_id = hosp_tab.keys.toList
            val filter_hosp_c0 = filter_ym_c0.filter(x => hos0_hosp_id.contains(x("HOSPITAL_CODE")))
            val filter_hosp_g0 = filter_ym_g0.filter(x => hos0_hosp_id.contains(x("HOSPITAL_CODE")))
            val c = innerJoin(m1_c,filter_hosp_c0,"min1","min1").map(mergeMC(_,market,hosp_tab))
            val g = innerJoin(m1_g,filter_hosp_g0,"min1","min1").map(mergeMC(_,market,hosp_tab))
            val t1_filter_group = (c ++ g).filter(_("Sales") != "")
                            .groupBy(x => x("ID").toString + x("Hosp_name") + x("Date") + x("Prod_Name") + x("Prod_CNAME") + x("HOSP_ID") + x("Strength") + x("DOI") + x("DOIE"))
            val t1 = t1_filter_group.map { x =>
                x._2.head ++ Map(
                    "Units" -> x._2.map(_ ("Units").asInstanceOf[Double]).sum,
                    "Sales" -> x._2.map(_ ("Sales").asInstanceOf[Double]).sum
                )
            }
            t1
        }

        val panel_local = writePanel(panel)
        parseMap = parseMap.filter(_._1 == company + user)

        toJson(panel_local)
    }

    private def filterSource(lst: List[Map[String, String]],ym: String): List[Map[String, String]] ={
        def getMin1Fun(tr: Map[String, String]): String = {
            tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME")
        }
        lst.filter(_("YM") == ym).map{x =>
            x ++ Map("min1" -> getMin1Fun(x))
        }
    }

    private def load_m1: List[Map[String, String]] ={
        val m1_file_local = file_config.path + company + file_config.product_vs_ims_file
        val completed = excelParser.readExcel(ExcelData(m1_file_local))
        completed.map{x =>
            Map("min1" -> x("min1"), "min1_标准" -> x("min1_标准"), "通用名" -> x("通用名"))
        }.distinct
    }

    private def load_hos00: List[Map[String, String]] ={
        val hos0_file_local = file_config.path + company + file_config.universe_inf_file
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
        val completed = excelParser.readExcel(ExcelData(hos0_file_local,fieldArg = setFieldMap))
        val filtered = completed.filter(filter)
        filtered.map{x =>
            x ++ Map("DOIE" -> x("DOI"))
        }
    }

    private def load_b0(market: String) = {
        val b0_file_local = file_config.path + company + file_config.markets_file
        excelParser.readExcel(ExcelData(b0_file_local,sheetName = market))
    }

    private def innerJoin(lst1: List[Map[String, String]],lst2: List[Map[String, String]],
                  nameBylst1: String, nameBylst2: String): List[Map[String, String]] = {
        for(
            r1 <- lst1; r2 <- lst2
            if r1(nameBylst1) == r2(nameBylst2)
        )yield{
            r1 ++ r2
        }
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

    private val mergeMC:(Map[String,String],String,Map[String,(String,String)]) => Map[String,Any] = { (old,market,hosId) =>
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

    private def writePanel(content: List[Map[String,Any]]): String = {
        val output_file_local = file_config.path + company + file_config.output + UUID.randomUUID.toString
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

        excelParser.writeByList(content, output_file_local, "Sheet1", writeSeq)
        output_file_local
    }
}