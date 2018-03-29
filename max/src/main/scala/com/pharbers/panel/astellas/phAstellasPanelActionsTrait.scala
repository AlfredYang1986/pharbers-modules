package com.pharbers.panel.astellas

import com.pharbers.panel.astellas.format._
import com.pharbers.paction.actionbase.pActionTrait
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

    // 1. read CPA文件第一页
    val cmd1 = xlsxReadingTrait[phAstellasCpaFormat](cpa_file, "cpa")

    //2. read GYCX文件第一页
    val cmd2 = xlsxReadingTrait[phAstellasGycxFormat](gycx_file, "gycx")

    //3. read 产品匹配表.xlsx
    val cmd3 = xlsxReadingTrait[phAstellasProductMatchFormat](product_match_file, "product_match_file")

    //4. read 市场匹配表.xlsx
    val cmd4 = xlsxReadingTrait[phAstellasMarketsMatchFormat](markets_match_file, "markets_match_file")

    //5. read universe_mkt_online.xlsx
    val cmd5 = xlsxReadingTrait[PhExcelXLSXCommonFormat](universe_file, "universe_file")


    override val actions: List[pActionTrait] = jarPreloadTrait() ::
            cmd1 :: cmd2 :: cmd3 :: cmd4 :: cmd5 ::
            phAstellasPanelImplAction(company, ym, mkt) ::
            Nil
}