package com.pharbers.unitTest

import java.util.UUID
import com.pharbers.pactions.actionbase.MapArgs

class startTest {
    val companyLst = List(
        "5b028f95ed925c2c705b85ba" -> List(
            Map(
                "company" -> "5b028f95ed925c2c705b85ba",
                "subsidiary" -> "辉瑞",
                "market" -> "INF",
                "ymInstance" -> "com.pharbers.panel.pfizer.phPfizerCalcYMJob",
                "panelInstance" -> "com.pharbers.panel.pfizer.phPfizerPanelJob",
                "maxInstance" -> "com.pharbers.calc.phMaxJob",

                "source" -> "cpa#gycx",
                "panelArgs" -> "universe_file#product_match_file#markets_match_file#fill_hos_data_file#pfc_match_file",
                "universe_file" -> "pfizer/universe_INF_online.xlsx",
                "product_match_file" -> "pfizer/产品标准化+vs+IMS_Pfizer_6市场others_0329.xlsx",
                "markets_match_file" -> "pfizer/通用名市场定义_0502.xlsx",
                "fill_hos_data_file" -> "pfizer/补充医院utf8_2018.txt",
                "pfc_match_file" -> "pfizer/PACKID生成panel.xlsx",
                "maxArgs" -> "universe_file"
            )
        )
    )



    def doTest(): Unit = companyLst.foreach{ c =>
        val args = Map(
            "company" -> c._1,
            "mkt" -> c._2.head("market"),
            "user" -> "user",
            "job_id" -> UUID.randomUUID().toString,
            "cpa" -> "pfizer/1802 CPA.xlsx",
            "gycx" -> "pfizer/1802 GYC.xlsx",
            "ym" -> "201802"
        )
        resultCheckJob(args).perform(MapArgs(Map().empty))
    }
}
