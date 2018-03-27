package com.pharbers.panel.astellas

import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.panel.astellas.format.PhXlsxCpaFormat
import com.pharbers.paction.actionContainer.pMapActionContainer
import com.pharbers.paction.format.input.common.PhExcelXLSXCommonFormat
import com.pharbers.paction.funcTrait.{jarPreloadTrait, xlsxReadingTrait}

trait phAstellasPanelActionsTrait extends pMapActionContainer {
    val company: String
    val cpa_file: String
    val gycx_file: String
    val ym: List[String]
    val mkt: String

    val product_match_file: String
    val markets_match_file: String
    val universe_file: String

    val cache_location: String

    // 1. read CPA文件第一页
    val cmd1 = xlsxReadingTrait[PhXlsxCpaFormat](cpa_file, "cpa")

    //2. read GYCX文件第一页
    val cmd2 = xlsxReadingTrait[PhXlsxCpaFormat](cpa_file, "gycx")

    //3. read 2017年未出版医院名单.xlsx
    val cmd3 = xlsxReadingTrait[PhExcelXLSXCommonFormat](product_match_file, "product_match_file")

    //4. read universe_麻醉市场_online.xlsx
    val cmd4 = xlsxReadingTrait[PhExcelXLSXCommonFormat](markets_match_file, "markets_match_file")

    //5. read 匹配表
    val cmd5 = xlsxReadingTrait[PhExcelXLSXCommonFormat](universe_file, "universe_file")


    override val actions: List[pActionTrait] = jarPreloadTrait() ::
            cmd1 :: cmd2 :: cmd3 :: cmd4 :: cmd5 ::
            phAstellasPanelImplAction(company, ym, mkt) ::
            Nil
}