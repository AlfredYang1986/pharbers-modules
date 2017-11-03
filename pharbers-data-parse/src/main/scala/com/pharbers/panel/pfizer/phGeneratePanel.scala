package com.pharbers.panel.pfizer

import java.util.UUID

import com.pharbers.memory.pages.pageMemory
import com.pharbers.panel.util.csv.phHandleCsv
import com.pharbers.panel.util.excel.{phExcelData, phHandleExcel}
import com.pharbers.panel.util.phDataHandle
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 17-10-24.
  */
case class phPfizerHandle(args: Map[String, List[String]]) extends phGeneratePanelTrait {
    override lazy val cpa = base_path + company + client_path + args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
    override lazy val gycx = base_path + company + client_path + args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head
    override val company = args.getOrElse("company", throw new Exception("no find company arg")).head
    override val markets = makets.split(",").toList
}

trait phGeneratePanelTrait extends phDataHandle with panel_file_path {
    protected val cpa: String
    protected val gycx: String
    protected val company: String
    protected val markets: List[String]

    def calcYM: JsValue = {
        def distinctYM(arg: (Map[String, String], List[String])): Map[String, Int] = {
            val temp = arg._1.map { ym =>
                val page = pageMemory(ym._2)

                val number = (for (i <- 0 until page.pageCount.toInt) yield {
                    page.pageData(i).map { line =>
                        val data = arg._2.zip(line.split(spl).toList).toMap
                        data("HOSPITAL_CODE")
                    }.distinct
                }).flatten.distinct.length

                page.ps.fs.closeStorage
                ym._1 -> number
            }

            val maxYM = temp.maxBy(x => x._2)
            temp.filter(_._2 > maxYM._2 / 2)
        }

        val filtered_c0 = distinctYM(loadCPA)
        val filtered_g0 = distinctYM(loadGYCX)
        val result = for (c <- filtered_c0; g <- filtered_g0
                          if c._1 == g._1) yield {
            (c._1, c._2 + g._2)
        }

        result.size match {
            case 0 => toJson("0")
            case 1 => toJson(result.head._1)
            case _ => toJson(result.keys.toList.mkString(comma))
        }
    }

    def getMarkets: JsValue = {
        markets.size match {
            case 0 => toJson("0")
            case 1 => toJson(markets.head)
            case _ => toJson(markets.mkString(comma))
        }
    }

    def getPanelFile(ym: List[String]): JsValue = {
        val c0 = loadCPA
        val g0 = loadGYCX
        val m1 = load_m1
        val result = ym.map { ym =>
            val c1 = fill_data(ym.takeRight(2).toInt, c0._1(ym), c0._2) //(c0._1(ym), c0._2)
            val g1 = (g0._1(ym), g0._2)
            val r1 = markets.map { mkt =>
                val lst = generatePanel(ym, mkt, c1, g1, m1)
                mkt -> toJson(lst)
            }.toMap
            ym -> toJson(r1)
        }.toMap

        toJson(result)
    }

    lazy val cache_base = base_path + company + cache_local
    val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
        val getMonth: String => String = {
            case i if i.toInt < 10 => "0" + i
            case i => i
        }
        Some(
            tr ++ Map(
                "min1" -> (tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME")),
                "YM" -> (tr("YEAR") + getMonth(tr("MONTH")))
            )
        )
    }

    def loadCPA: (Map[String, String], List[String]) = {
        val setDefaultMap = Map(
            "PRODUCT_NAME" -> "$MOLE_NAME",
            "VALUE" -> "0",
            "STANDARD_UNIT" -> "0"
        )
        implicit val postArg = postFun
        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
        implicit val cacheLocalArg = cache_base
        phHandleExcel().readExcelToCache(phExcelData(cpa, defaultValueArg = setDefaultMap), "YM")
    }

    def loadGYCX: (Map[String, String], List[String]) = {
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
        val setDefaultMap = Map(
            "PRODUCT_NAME" -> "$MOLE_NAME",
            "VALUE" -> "0",
            "STANDARD_UNIT" -> "0"
        )
        implicit val postArg = postFun
        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
        implicit val cacheLocalArg = cache_base
        phHandleExcel().readExcelToCache(phExcelData(gycx, defaultValueArg = setDefaultMap, fieldArg = setFieldMap), "YM")
    }

    def load_m1: List[Map[String, String]] = {
        val m1_file_local = base_path + company + product_vs_ims_file
        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
            Some(
                Map("min1" -> tr("min1"), "min1_标准" -> tr("min1_标准"), "通用名" -> tr("通用名"))
            )
        }
        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
        phHandleExcel().readExcel(phExcelData(m1_file_local))
    }

    def load_hos00(market: String): List[Map[String, String]] = {
        val hos0_file_local = base_path + company + universe_inf_file.replace("???", market)
        val setFieldMap = Map(
            "样本医院编码" -> "ID",
            "PHA医院名称" -> "HOSP_NAME",
            "PHA ID" -> "HOSP_ID",
            "市场" -> "DOI"
        )
        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
            Some(
                Map(
                    "ID" -> tr("ID"),
                    "HOSP_NAME" -> tr("HOSP_NAME"),
                    "HOSP_ID" -> tr("HOSP_ID"),
                    "DOI" -> tr("DOI"),
                    "DOIE" -> tr("DOI")
                )
            )
        }
        implicit val filter: Map[String, String] => Boolean = { tr =>
            tr.get("If Panel_All") match {
                case None => false
                case Some(s) if s == "1" => true
                case _ => false
            }
        }
        phHandleExcel().readExcel(phExcelData(hos0_file_local, fieldArg = setFieldMap))
    }

    def load_b0(market: String): List[Map[String, String]] = {
        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
        implicit val postArg = com.pharbers.panel.util.excel.phHandleExcel.postFun
        val b0_file_local = base_path + company + markets_file
        phHandleExcel().readExcel(phExcelData(b0_file_local, sheetName = "Sheet1"))
                        .filter(_("Market") == market)
    }

    def innerJoin(lst1: Stream[Map[String, String]], lst2: Stream[Map[String, String]],
                  nameBylst1: String, nameBylst2: String): Stream[Map[String, String]] = {
        for (
            r1 <- lst1; r2 <- lst2
            if r1(nameBylst1) == r2(nameBylst2)
        ) yield {
            r1 ++ r2
        }
    }

    def fill_data(m: Int, file: String, title: List[String]): (String, List[String]) = {
        implicit val titleArg = title
        val lst = fill_hos_lst(m)
        val cache_file = cache_base + UUID.randomUUID.toString + ".cache"

        val cpa_page = pageMemory(file)
        ( 0 until cpa_page.pageCount.toInt ) foreach { i =>
            cpa_page.pageData(i).foreach { line =>
                val data = title.zip(line.split(spl).toList).toMap
                if(!lst.contains(data("HOSPITAL_CODE")))
                    phHandleCsv().appendByLine(data, cache_file)
            }
        }
        cpa_page.ps.fs.closeStorage

        append_fill_data(m, lst, cache_file)

        (cache_file, title)
    }

    def append_fill_data(m: Int, hos_lst: List[String], cache_file: String)
                        (implicit title: List[String]) = {
        val fill_data_local = base_path + company + fill_hos_data_file
        val fill_data_page = pageMemory(fill_data_local)

        val fd_title = fill_data_page.pageData(0).head
                .dropRight(1).toUpperCase
                .split(spl).toList

        (0 until fill_data_page.pageCount.toInt) foreach { i =>
            fill_data_page.pageData(i).foreach { line =>
                val data = fd_title.zip(line.dropRight(1).filter(_ != '"').split(spl).toList).toMap
                if (hos_lst.contains(data("HOSPITAL_CODE")) && data("MONTH") == m.toString)
                    phHandleCsv().appendByLine(postFun(data).get, cache_file)
            }
        }

        fill_data_page.ps.fs.closeStorage
    }

    def fill_hos_lst(m: Int) = {
        def load_uc_hos: List[Map[String, String]] = {
            implicit val postArg = com.pharbers.panel.util.excel.phHandleExcel.postFun
            implicit val filter: Map[String, String] => Boolean = { tr =>
                tr.get("月份") match {
                    case None => false
                    case Some(s) if s != "" => true
                    case _ => false
                }
            }
            phHandleExcel().readExcel(phExcelData(cpa, 2))
        }
        def load_up_hos: List[Map[String, String]] = {
            implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
            implicit val postArg = com.pharbers.panel.util.excel.phHandleExcel.postFun
            val unpublished_hos_file_local = base_path + company + unpublished_hos_file
            phHandleExcel().readExcel(phExcelData(unpublished_hos_file_local,3))
        }

        val up_hos = load_up_hos.map(_("id"))
        val nc_hos = load_uc_hos
        (nc_hos.map { h =>
            if (h("月份").split("、").map(_.toInt).contains(m))
                h("医院编码")
            else
                ""
        }.filter(_ != "") ++ up_hos).distinct
    }

    def generatePanel(ym: String, market: String,
                      c1: (String, List[String]), g1: (String, List[String]),
                      m1: List[Map[String, String]]) = {
        val hos00 = load_hos00(market)
        val b0 = load_b0(market)
        val m2 = innerJoin(b0.toStream, m1.toStream, "通用名_原始", "通用名").map(mergeMB(_))
        val hosp_tab = getHospTab(hos00, market)

        val cpa_panel = generate(c1, m2, ym, market, hosp_tab)
        generate(g1, m2, ym, market, hosp_tab, cpa_panel)
    }

    private def getHospTab(hos00: List[Map[String, String]], market: String) = {
        hos00.filter(_ ("DOI") == market + " Market").groupBy(_ ("ID"))
                .flatMap { x =>
                    Map(x._1 -> (x._2.head("HOSP_NAME"), x._2.head("HOSP_ID")))
                }
    }

    private val mergeMB: Map[String, String] => Map[String, String] = { old =>
        Map(
            "min1" -> old("min1"),
            "min1_标准" -> old("min1_标准"),
            "marketname" -> old("Market")
        )
    }

    private def generate(source: (String, List[String]),
                     m1Arg: Stream[Map[String, String]],
                     ym: String, market: String,
                     hosp_tab: Map[String, (String, String)],
                     panel_lst_arg: List[String] = Nil): List[String] = {

        implicit val base_panel_local: String = base_path + company + output_local
        implicit val panelTitleSeq: List[String] = "ID" :: "Hosp_name" :: "Date" :: "Prod_Name" :: "Prod_CNAME" ::
                "HOSP_ID" :: "Strength" :: "DOI" :: "DOIE" :: "Units" :: "Sales" :: Nil

        val hos0_hosp_id = hosp_tab.keys.toList
        val page = pageMemory(source._1)
        var file_lst = panel_lst_arg

        (0 until page.pageCount.toInt) foreach { i =>
            lazy val temp = page.pageData(i).map { line =>
                val data = source._2.zip(line.split(spl).toList).toMap
                if (data("YM") == ym && hos0_hosp_id.contains(data("HOSPITAL_CODE")))
                    data
                else
                    Map[String, String]()
            }.filter(_ != Map())

            innerJoin(m1Arg, temp, "min1", "min1")
                    .map(mergeMC(_, market, hosp_tab))
                    .filter(_ ("Sales") != "")
                    .foreach { x =>
                        file_lst = file_lst :+ phHandleCsv().sortInsert(x, file_lst, distinct_source, mergeSameLine)
                        file_lst = file_lst.distinct
                    }
        }

        page.ps.fs.closeStorage
        file_lst
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

        if(cur.toString == "") -1
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
