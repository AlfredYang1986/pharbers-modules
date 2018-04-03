package com.pharbers.panel.nhwa

import com.pharbers.common.excel.input.{PhExcelXLSXCommonFormat, PhXlsxSecondSheetFormat, PhXlsxThirdSheetFormat}
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.panel.nhwa.format.PhXlsxCpaFormat
import com.pharbers.pactions.actionContainer.pActionContainer
import com.pharbers.pactions.generalactions.{jarPreloadAction, saveCurrenResultAction, xlsxReadingAction}

trait phNhwaPanelActionsTrait extends pActionContainer {
    val company: String
    val cpa_file: String
    val ym: List[String]
    val mkt: String

    val product_match_file: String
    val markets_match_file: String
    val universe_file: String
    val not_published_hosp_file: String
    val full_hosp_file: String

    val cache_location: String

    /**
      * 1. read 2017年未出版医院名单.xlsx
      */
    val cmd1 = xlsxReadingAction[PhExcelXLSXCommonFormat](not_published_hosp_file, "not_published_hosp_file")
    val cmd10 = saveCurrenResultAction(cache_location + cmd1.name)

    /**
      * 2. read universe_麻醉市场_online.xlsx
      */
    val cmd2 = xlsxReadingAction[PhExcelXLSXCommonFormat](universe_file, "universe_file")
    val cmd20 = saveCurrenResultAction(cache_location + cmd2.name)

    /**
      * 3. read 匹配表
      */
    val cmd3 = xlsxReadingAction[PhExcelXLSXCommonFormat](product_match_file, "product_match_file")
    val cmd30 = saveCurrenResultAction(cache_location + cmd3.name)

    /**
      * 4. read 补充医院
      */
    val cmd4 = xlsxReadingAction[PhXlsxCpaFormat](full_hosp_file, "full_hosp_file")
    val cmd40 = saveCurrenResultAction(cache_location + cmd4.name)

    /**
      * 5. read 通用名市场定义, 读第三页
      */
    val cmd5 = xlsxReadingAction[PhXlsxThirdSheetFormat](markets_match_file, "markets_match_file")
    val cmd50 = saveCurrenResultAction(cache_location + cmd5.name)


    /**
      * 6. read CPA文件第一页
      */
    val cmd6 = xlsxReadingAction[PhXlsxCpaFormat](cpa_file, "cpa")
    val cmd60 = saveCurrenResultAction(cache_location + cmd6.name)

    /**
      * 7. read CPA文件第二页
      */
    val cmd7 = xlsxReadingAction[PhXlsxSecondSheetFormat](cpa_file, "not_arrival_hosp_file")
    val cmd70 = saveCurrenResultAction(cache_location + cmd7.name)

    override val actions: List[pActionTrait] = jarPreloadAction() ::
            cmd1 ::cmd10 ::
            cmd2 ::cmd20 ::
            cmd3 :: cmd30 ::
            cmd4 :: cmd40 ::
            cmd5 :: cmd50 ::
            cmd6 :: cmd60 ::
            cmd7 :: cmd70 ::
            phNhwaPanelImplAction(company, cache_location, ym, mkt) ::
            Nil
}