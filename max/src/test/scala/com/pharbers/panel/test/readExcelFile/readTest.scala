package com.pharbers.panel.test.readExcelFile

import java.io.FileInputStream

import com.pharbers.calc.phMaxJob
import com.pharbers.pactions.actionbase.MapArgs
import com.pharbers.panel.astellas.phAstellasPanelJob
import com.pharbers.panel.nhwa.phNhwaPanelJob
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object readTest extends App {
//  val panelResult = phAstellasPanelJob("/mnt/config/Client/astl_cpa-12.xlsx", "/mnt/config/Client/astl_gycx_1-12.xlsx", "201712", "Allelock").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
val panelResult = phNhwaPanelJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx", "201712", "麻醉市场").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get.toString
  println(panelResult)
  val maxResultMap = phMaxJob(panelResult, "astellas/UNIVERSE_Allelock_online.xlsx").perform().asInstanceOf[MapArgs].get("max_bson_action").get
  println(maxResultMap)
}
