package com.pharbers.panel

import com.pharbers.paction.actionContainer.{pActionContainer, pMapActionContainer}
import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.paction.format.input.common.PhExcelXLSXCommonFormat
import com.pharbers.paction.funcTrait.{excelReadingTrait, jarPreloadTrait, saveCurrenResultTrait}

trait NhwaPanelActions extends pActionContainer {
    val file_local = "/mnt/config/FileBase/235f39e3da85c2dee2a2b20d004a8b77/Client/170322安斯泰来1月底层检索.xls"
    val file_local2 = "/mnt/config/FileBase/8ee0ca24796f9b7f284d931650edbd4b/Client/171215恩华2017年10月检索.xlsx"

    val file_local_dir = "resource/nhwa/"
    val file_local_result = "resource/result/"

    /**
      * 1. read 2017年未出版医院名单.xlsx
      */
    val cmd1 = excelReadingTrait[PhExcelXLSXCommonFormat]("resource/nhwa/2017年未出版医院名单.xlsx", "2017年未出版医院名单")
    val cmd10 = saveCurrenResultTrait(file_local_result + cmd1.name)

    /**
      * 2. read universe_麻醉市场_online.xlsx
      */
//    val cmd2 = excelReadingTrait[PhExcelXLSXCommonFormat]("resource/nhwa/universe_麻醉市场_online.xlsx", "universe_麻醉市场_online")
//    val cmd20 = saveCurrenResultTrait(file_local_result + cmd2.name)

    /**
      * 3. read 匹配表
      */
//    val cmd3 = excelReadingTrait[PhExcelXLSXCommonFormat]("resource/nhwa/匹配表.xlsx", "匹配表")
//    val cmd30 = saveCurrenResultTrait(file_local_result + cmd3.name)

    /**
      * 4. read 补充医院
      */
//    val cmd4 = excelReadingTrait[PhExcelXLSXCommonFormat]("resource/nhwa/补充医院.xlsx", "补充医院")
//    val cmd40 = saveCurrenResultTrait(file_local_result + cmd4.name)

    /**
      * 5. read 通用名市场定义
      */
//    val cmd5 = excelReadingTrait[PhExcelXLSXCommonFormat]("resource/nhwa/通用名市场定义.xlsx", "通用名市场定义")
//    val cmd50 = saveCurrenResultTrait(file_local_result + cmd5.name)

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
                                                cmd1 ::cmd10 ::
//                                                cmd2 :: cmd20 ::
//                                                cmd3 :: cmd30 ::
//                                                cmd4 :: cmd40 ::
//                                                cmd5 :: cmd50 ::
                                                Nil
}