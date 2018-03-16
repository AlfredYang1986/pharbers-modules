package com.pharbers.panel.nhwa

import java.util.UUID
import scala.collection.immutable.Map
import com.pharbers.panel.phPanelFilePath
import com.pharbers.panel.util.phUtilManage
import com.pharbers.spark.driver.phSparkDriver
import com.pharbers.panel.util.excel.phExcelFileInfo

/**
  * Created by clock on 18-3-7.
  */
case class phNhwaHandle(args: Map[String, List[String]]) extends phNhwaCalcYm with phNhwaPanel with phPanelFilePath{
    override val sparkDriver: phSparkDriver = phSparkDriver()

    val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head

    override val m1_location = base_path + company + product_match_file
    override val b0_location = base_path + company + markets_match_file
    override val hos_location = base_path + company + universe_file
    override val not_arrival_hosp_location = base_path + company + not_arrival_hosp_file
    override val not_published_hosp_location = base_path + company + not_published_hosp_file
    override val full_hosp_location = base_path + company + fill_hos_data_file

    override val cpa_location: String = excel2csv
    override val output_location: String = base_path + company + output_dir

    private def excel2csv: String = {
        val excel_file = base_path + company + source_dir + args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
        val output_file = base_path + company + source_dir + UUID.randomUUID.toString + ".csv"

        val setFieldMap = Map(
            "省" -> "PROVINCES",
            "城市" -> "CITY",
            "年" -> "YEAR",
            "季度" -> "QUARTER",
            "月" -> "MONTH",
            "医院编码" -> "HOSPITAL_CODE",
            "ATC编码" -> "ATC_CODE",
            "ATC码" -> "ATC_CODE",
            "药品名称" -> "MOLE_NAME",
            "商品名" -> "PRODUCT_NAME",
            "包装" -> "PACKAGE",
            "药品规格" -> "PACK_DES",
            "规格" -> "PACK_DES",
            "包装数量" -> "PACK_NUMBER",
            "金额（元）" -> "VALUE",
            "数量（支/片）" -> "STANDARD_UNIT",
            "剂型" -> "APP2_COD",
            "给药途径" -> "APP1_COD",
            "途径" -> "APP1_COD",
            "生产企业" -> "CORP_NAME"
        )
        val setDefaultMap = Map(
            "PRODUCT_NAME" -> "$MOLE_NAME",
            "VALUE" -> "0",
            "STANDARD_UNIT" -> "0"
        )
        val getMonth: String => String = {
            case i if i.toInt < 10 => "0" + i
            case i => i
        }
        val excel = phExcelFileInfo(file_local = excel_file, fieldArg = setFieldMap, defaultValueArg = setDefaultMap)
        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
            Some(
                tr ++ Map(
                    "min1" -> (tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME")),
                    "YM" -> (tr("YEAR") + getMonth(tr("MONTH")))
                )
            )
        }

        phUtilManage().excel2Csv(excel, output_file)
        output_file
    }
}